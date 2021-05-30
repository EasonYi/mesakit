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
import com.telenav.tdk.data.formats.library.map.identifiers.MapIdentifier;
import com.telenav.tdk.data.formats.library.map.identifiers.MapRelationIdentifier;
import com.telenav.tdk.graph.Graph;
import com.telenav.tdk.graph.GraphElement;

/**
 * Identifier of relations in a map graph
 *
 * @author jonathanl (shibo)
 */
public class RelationIdentifier extends MapRelationIdentifier implements GraphElementIdentifier
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static SwitchParser.Builder<RelationIdentifier> switchParser(final String name, final String description)
    {
        return new Converter(LOGGER).switchParser(RelationIdentifier.class, name, description);
    }

    public static class Converter extends Quantizable.Converter<RelationIdentifier>
    {
        public Converter(final Listener<Message> listener)
        {
            super(listener, RelationIdentifier::new);
        }
    }

    /**
     * Construct from identifier
     */
    public RelationIdentifier(final long identifier)
    {
        super(identifier);
    }

    @Override
    public GraphElement element(final Graph graph)
    {
        return graph.relationForIdentifier(this);
    }

    /**
     * @return The next identifier
     */
    @Override
    public RelationIdentifier next()
    {
        return new RelationIdentifier(asLong() + 1);
    }

    @Override
    protected MapIdentifier newIdentifier(final long identifier)
    {
        return new RelationIdentifier(identifier);
    }
}
