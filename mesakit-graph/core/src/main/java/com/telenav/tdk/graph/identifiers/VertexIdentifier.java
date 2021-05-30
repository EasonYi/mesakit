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

package com.telenav.tdk.graph.identifiers;

import com.telenav.tdk.core.kernel.commandline.SwitchParser;
import com.telenav.tdk.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.tdk.core.kernel.logging.Logger;
import com.telenav.tdk.core.kernel.logging.LoggerFactory;
import com.telenav.tdk.core.kernel.messaging.Listener;
import com.telenav.tdk.core.kernel.messaging.Message;
import com.telenav.tdk.core.kernel.scalars.counts.Bits;
import com.telenav.tdk.core.kernel.scalars.identifiers.IntegerIdentifier;
import com.telenav.tdk.graph.Edge;
import com.telenav.tdk.graph.Graph;
import com.telenav.tdk.graph.GraphElement;
import com.telenav.tdk.graph.Vertex;

/**
 * Identifier of {@link Vertex}es in a {@link Graph}. The valid range for a vertex identifier is from 1 to {@link
 * Integer#MAX_VALUE}. Zero is not a valid vertex identifier because it is the Java uninitialized value.
 * <p>
 * Note that this is NOT the same as an PBF node identifier because Graph API {@link Vertex}es only occur at {@link
 * Edge} end-points ('from' and 'to' nodes).
 *
 * @author jonathanl (shibo)
 */
public class VertexIdentifier extends IntegerIdentifier implements GraphElementIdentifier
{
    /**
     * Although node identifiers can exceed 32 bits, the total number of Graph API {@link VertexIdentifier}s is much
     * less than that because vertex identifiers in the Graph API are limited to {@link Edge} intersection points of
     * which there are no more than two times the number of edges or log2(~300M * 2) = 30 bits. We add two extra bits to
     * allow for future proofing.
     * <p>
     * NOTE: The primitive value is present due to limitations in Java annotations (which reference this constant).
     */
    public static final Bits SIZE = Bits._32;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static SwitchParser.Builder<VertexIdentifier> switchParser(final String name, final String description)
    {
        return new Converter(LOGGER).switchParser(VertexIdentifier.class, name, description);
    }

    public static class Converter extends Quantizable.Converter<VertexIdentifier>
    {
        public Converter(final Listener<Message> listener)
        {
            super(listener, identifier ->
            {
                if (identifier > 0 && identifier < Integer.MAX_VALUE)
                {
                    return new VertexIdentifier(identifier.intValue());
                }
                return null;
            });
        }
    }

    public VertexIdentifier(final int identifier)
    {
        super(identifier);
        assert identifier > 0;
    }

    /**
     * Implementation of {@link GraphElementIdentifier#element(Graph)}.
     *
     * @return The graph element from the given graph for this identifier
     */
    @Override
    public GraphElement element(final Graph graph)
    {
        return graph.vertexForIdentifier(this);
    }

    /**
     * @return The next vertex identifier higher than this one
     */
    public VertexIdentifier next()
    {
        return new VertexIdentifier(asInteger() + 1);
    }
}
