////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.tdk.graph.specifications.common.graph.loader;

import com.telenav.tdk.core.filesystem.*;
import com.telenav.tdk.core.kernel.interfaces.naming.Named;
import com.telenav.tdk.core.kernel.logging.*;
import com.telenav.tdk.core.kernel.messaging.Message;
import com.telenav.tdk.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.tdk.core.kernel.operation.progress.ProgressReporter;
import com.telenav.tdk.core.kernel.time.Time;
import com.telenav.tdk.core.resource.path.Extension;
import com.telenav.tdk.graph.*;
import com.telenav.tdk.graph.io.archive.GraphArchive;

import static com.telenav.tdk.core.kernel.validation.Validate.ensure;
import static com.telenav.tdk.core.resource.compression.archive.ZipArchive.Mode.*;

/**
 * An OSM PBF resource that can be converted to a graph by calling {@link #graph()}.
 *
 * @author jonathanl (shibo)
 */
public class PbfFile extends BaseRepeater<Message> implements Named
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static boolean accepts(final File file)
    {
        final var fileName = file.fileName();
        if (fileName != null)
        {
            final var extension = fileName.compoundExtension();
            return extension != null && (extension.endsWith(Extension.OSM_PBF));
        }
        return false;
    }

    /** The file to convert */
    private final File file;

    /** The converter configuration */
    private final PbfToGraphConverter.Configuration configuration;

    /**
     * @param file The .osm.pbf input file
     * @param configuration The pbf converter configuration
     */
    public PbfFile(final File file, final PbfToGraphConverter.Configuration configuration)
    {
        ensure(accepts(file), "Resource '$' is not a $", file, Extension.GRAPH);

        this.configuration = configuration;
        this.file = file;
    }

    /**
     * @return The graph for this PBF resource
     */
    public Graph graph(final ProgressReporter reporter)
    {
        final var metadata = Metadata.from(file);
        if (metadata != null)
        {
            final var output = Folder.temporaryForProcess(Folder.Type.CLEAN_UP_ON_EXIT)
                    .temporaryFile(file.fileName().withoutExtension(Extension.OSM_PBF)
                            .withoutExtension(new Extension(".pbf.gz")), Extension.GRAPH);

            final var converter = (PbfToGraphConverter) metadata.dataSpecification().newGraphConverter(metadata);
            converter.configure(configuration);

            // Convert to a graph
            final var graph = converter.convert(file);
            if (graph != null)
            {
                // save to disk,
                reporter.phase("Writing");
                try (final var archive = new GraphArchive(output, reporter, WRITE))
                {
                    graph.save(archive);
                }
                LOGGER.information("Converted $ to $", file, output);

                // then reload it
                reporter.phase("Loading");
                @SuppressWarnings("resource") final var archive = new GraphArchive(output, reporter, READ);
                return archive.load(this);
            }
            else
            {
                problem("Unable to convert $ to graph", file);
                return null;
            }
        }

        problem("Unable to load metadata from '$'", file);
        return null;
    }

    public Time lastModified()
    {
        return file.lastModified();
    }

    @Override
    public String name()
    {
        return file.fileName().name();
    }

    @Override
    public String toString()
    {
        return name();
    }
}
