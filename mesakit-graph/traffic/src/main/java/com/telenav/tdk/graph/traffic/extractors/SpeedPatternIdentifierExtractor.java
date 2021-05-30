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

package com.telenav.tdk.graph.traffic.extractors;

import com.telenav.tdk.core.data.extraction.BaseExtractor;
import com.telenav.tdk.core.kernel.language.object.Pair;
import com.telenav.tdk.core.kernel.messaging.*;
import com.telenav.tdk.data.formats.pbf.model.tags.PbfWay;
import com.telenav.tdk.graph.traffic.historical.SpeedPatternIdentifier;

public class SpeedPatternIdentifierExtractor extends BaseExtractor<Pair<SpeedPatternIdentifier>, PbfWay>
{
    public SpeedPatternIdentifierExtractor(final Listener<Message> listener)
    {
        super(listener);
    }

    @Override
    public Pair<SpeedPatternIdentifier> onExtract(final PbfWay way)
    {
        final var isReversedOneWay = way.tagValueIsNegativeOne("oneway");

        return new Pair<>(isReversedOneWay ? null :
                extractSpeedPatternIdentifier(way, "spd_id:f"),
                extractSpeedPatternIdentifier(way, "spd_id:t"));
    }

    private SpeedPatternIdentifier extractSpeedPatternIdentifier(final PbfWay way, final String key)
    {
        final var identifier = way.tagValueAsNaturalNumber(key);
        return identifier < 0 ? null : new SpeedPatternIdentifier((int) identifier);
    }
}
