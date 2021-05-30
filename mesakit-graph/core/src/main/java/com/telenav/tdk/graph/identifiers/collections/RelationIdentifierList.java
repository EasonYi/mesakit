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

package com.telenav.tdk.graph.identifiers.collections;

import com.telenav.tdk.core.kernel.conversion.string.BaseStringConverter;
import com.telenav.tdk.core.kernel.language.collections.list.ObjectList;
import com.telenav.tdk.core.kernel.language.string.StringList;
import com.telenav.tdk.core.kernel.language.string.formatting.Separators;
import com.telenav.tdk.core.kernel.messaging.Listener;
import com.telenav.tdk.core.kernel.messaging.Message;
import com.telenav.tdk.core.kernel.scalars.counts.Maximum;
import com.telenav.tdk.data.formats.library.map.identifiers.MapRelationIdentifier;
import com.telenav.tdk.data.formats.pbf.model.identifiers.PbfRelationIdentifier;

import java.util.List;

public class RelationIdentifierList extends ObjectList<MapRelationIdentifier>
{
    public static class Converter extends BaseStringConverter<RelationIdentifierList>
    {
        private final Separators separators;

        public Converter(final Listener<Message> listener, final Separators separators)
        {
            super(listener);
            this.separators = separators;
        }

        @Override
        protected RelationIdentifierList onConvertToObject(final String value)
        {
            final var split = StringList.split(value, separators.current());
            final var identifiers = new ObjectList<MapRelationIdentifier>();
            final var converter = new PbfRelationIdentifier.Converter(this);
            for (final var at : split)
            {
                identifiers.add(converter.convert(at));
            }
            return new RelationIdentifierList(identifiers);
        }

        @Override
        protected String onConvertToString(final RelationIdentifierList value)
        {
            return value.join(separators.current());
        }
    }

    public RelationIdentifierList(final List<MapRelationIdentifier> identifiers)
    {
        super(Maximum.of(identifiers.size()));
        appendAll(identifiers);
    }

    public RelationIdentifierList(final Maximum maximumSize)
    {
        super(maximumSize);
    }
}
