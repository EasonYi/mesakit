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


package com.telenav.tdk.graph.specifications.osm.graph.edge.model.attributes.extractors;

import com.telenav.tdk.core.data.extraction.BaseExtractor;
import com.telenav.tdk.core.kernel.messaging.*;
import com.telenav.tdk.data.formats.pbf.model.tags.PbfWay;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;

public class LastModifierExtractor extends BaseExtractor<OsmUser, PbfWay>
{
    public LastModifierExtractor(final Listener<Message> listener)
    {
        super(listener);
    }

    @Override
    public OsmUser onExtract(final PbfWay object)
    {
        return object.user();
    }
}
