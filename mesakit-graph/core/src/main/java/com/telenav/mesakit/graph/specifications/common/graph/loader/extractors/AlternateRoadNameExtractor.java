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

package com.telenav.mesakit.graph.specifications.common.graph.loader.extractors;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.mesakit.map.data.formats.pbf.model.entities.PbfWay;
import com.telenav.mesakit.map.region.locale.MapLocale;
import com.telenav.mesakit.map.road.model.RoadName;
import com.telenav.mesakit.map.road.name.standardizer.RoadNameStandardizer;

import java.util.ArrayList;
import java.util.List;

public class AlternateRoadNameExtractor extends BaseRoadNameExtractor
{
    private static final String[] KEYS = new String[]
            {
                    "alt",
                    "altname",
                    "alt_name_1",
                    "alt_name_2",
            };

    public AlternateRoadNameExtractor(MapLocale locale, RoadNameStandardizer.Mode mode,
                                      Listener listener)
    {
        super(locale, mode, listener);
    }

    @Override
    public List<RoadName> onExtract(PbfWay way)
    {
        List<RoadName> names = new ArrayList<>();

        addRoadName(names, way.tagValue("alt_name"));
        addRoadName(names, way.tagValue("reg_name"));
        addRoadName(names, way.tagValue("loc_name"));
        addRoadName(names, way.tagValue("old_name"));

        addRoadNameTranslations(names, way, KEYS);

        return names;
    }
}
