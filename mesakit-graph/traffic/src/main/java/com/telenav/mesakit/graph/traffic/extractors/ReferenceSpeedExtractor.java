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

package com.telenav.mesakit.graph.traffic.extractors;

import com.telenav.kivakit.kernel.data.extraction.BaseExtractor;
import com.telenav.kivakit.kernel.language.objects.Pair;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.mesakit.map.data.formats.pbf.model.entities.PbfWay;
import com.telenav.mesakit.map.measurements.motion.Speed;

public class ReferenceSpeedExtractor extends BaseExtractor<Pair<Speed>, PbfWay>
{
    public ReferenceSpeedExtractor(final Listener listener)
    {
        super(listener);
    }

    @Override
    public Pair<Speed> onExtract(final PbfWay way)
    {
        final var isReversedOneWay = way.tagValueIsNegativeOne("oneway");

        return new Pair<>(isReversedOneWay ? null :
                extractSpeed(way, "spd_kph:f"),
                extractSpeed(way, "spd_kph:t"));
    }

    private Speed extractSpeed(final PbfWay way, final String key)
    {
        final var speed = way.tagValueAsNaturalNumber(key);
        return speed < 0 ? null : Speed.kilometersPerHour(speed);
    }
}
