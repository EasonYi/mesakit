////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.aonia.map.cutter;

import com.telenav.aonia.map.cutter.project.lexakai.diagrams.DiagramMapCutter;
import com.telenav.aonia.map.data.formats.pbf.model.entities.PbfNode;
import com.telenav.aonia.map.data.formats.pbf.model.entities.PbfRelation;
import com.telenav.aonia.map.data.formats.pbf.model.entities.PbfWay;
import com.telenav.aonia.map.data.formats.pbf.processing.writers.PbfWriter;
import com.telenav.aonia.map.geography.Location;
import com.telenav.aonia.map.region.Region;
import com.telenav.aonia.map.region.RegionSet;
import com.telenav.kivakit.core.collections.primitive.set.SplitLongSet;
import com.telenav.kivakit.core.kernel.language.values.count.Estimate;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("rawtypes")
@UmlClassDiagram(diagram = DiagramMapCutter.class, includeProtectedMethods = false)
public abstract class Cut
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private final PbfRegionCutter extractor;

    private final ConcurrentMap<Region, PbfWriter> writerForRegion = new ConcurrentHashMap<>();

    private final Set<Resource> outputResources = new HashSet<>();

    protected Cut(final PbfRegionCutter extractor)
    {
        this.extractor = extractor;
    }

    public abstract Set<Resource> cut();

    protected void closeWriters()
    {
        for (final var writer : writerForRegion.values())
        {
            writer.close();
        }
    }

    protected PbfRegionCutter extractor()
    {
        return extractor;
    }

    protected boolean include(final PbfWay way)
    {
        return extractor().wayFilter().accepts(way);
    }

    protected Integer indexForRegion(final Region region)
    {
        return extractor().regionIndexMap().indexForRegion(region);
    }

    protected Location location(final PbfNode node)
    {
        return Location.degrees(node.latitude(), node.longitude());
    }

    protected Location location(final WayNode node)
    {
        return Location.degrees(node.getLatitude(), node.getLongitude());
    }

    protected SplitLongSet newSplitLongSet(final String name)
    {
        final var set = new SplitLongSet(name);
        set.initialSize(Estimate._65536);
        set.initialize();
        return set;
    }

    protected Set<Resource> outputResources()
    {
        return outputResources;
    }

    protected Region regionForIndex(final Integer index)
    {
        return extractor().regionIndexMap().regionForIndex(index);
    }

    protected RegionSet regions(final PbfWay way)
    {
        final var regions = new RegionSet();
        for (final var node : way.nodes())
        {
            regions.addAll(regionsForLocation(location(node)));
        }
        return regions;
    }

    /**
     * @return The list of regions that contain the given location (in the world graph, this will always be a single
     * cell).
     * <p>
     * NOTE: Historically, regions could nest to allow nested administrative borders like states and countries (and that
     * code, while no longer used, should still work, albeit VERY inefficiently), but now regions are non-overlapping,
     * non-nested grid cells in the world graph)
     */
    protected List<Region> regionsForLocation(final Location location)
    {
        return extractor.regionsForLocation(location);
    }

    protected PbfWay withTelenavSoftCutTag(final PbfWay way)
    {
        final List<Tag> tags = new ArrayList<>(way.get().getTags());
        tags.add(new Tag("telenav:softcut", "true"));
        return way.withTags(tags);
    }

    protected void write(final PbfNode node, final List<Region> regions)
    {
        if (regions != null)
        {
            // Go through each region,
            for (final var region : regions)
            {
                // and if we're supposed to extract the region
                if (extractor().regionsToExtract().contains(region))
                {
                    // get the write for the region
                    final var writer = writer(region);

                    // and write the node
                    if (writer != null)
                    {
                        writer.write(node);
                    }
                }
            }
        }
    }

    protected void write(final PbfRelation relation, final Iterable<Region> regions)
    {
        if (regions != null)
        {
            // Go through each region,
            for (final var region : regions)
            {
                // and if we're supposed to extract the region
                if (extractor().regionsToExtract().contains(region))
                {
                    // and write out the relation (which may reference missing ways from
                    // neighboring regions) to the output file for the region
                    final var writer = writer(region);
                    if (writer != null)
                    {
                        writer.write(relation);
                    }
                }
            }
        }
    }

    protected void write(final PbfWay way, final Iterable<Region> regions, final boolean softcut)
    {
        if (regions != null)
        {
            // Go through each region,
            for (final var region : regions)
            {
                // and if we're supposed to extract the region
                if (extractor().regionsToExtract().contains(region))
                {
                    // get the writer for the region
                    final var writer = writer(region);
                    if (writer != null)
                    {
                        // and write the way, with telenav:softcut=true if the way is soft-cut
                        writer.write(softcut ? withTelenavSoftCutTag(way) : way);
                    }
                }
            }
        }
    }

    protected PbfWriter writer(final Region region)
    {
        return writerForRegion.computeIfAbsent(region, this::openWriter);
    }

    private PbfWriter openWriter(final Region region)
    {
        var folder = extractor.outputFolder();
        final var regionFolder = region.folder();
        if (regionFolder != null)
        {
            folder = folder.folder(regionFolder);
        }
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        final var output = folder.file(region.name()).withExtension(Extension.OSM_PBF);
        DEBUG.trace("Opened stream #$ to $", writerForRegion.size() + 1, output);
        outputResources.add(output);
        return new PbfWriter(output, true);
    }
}
