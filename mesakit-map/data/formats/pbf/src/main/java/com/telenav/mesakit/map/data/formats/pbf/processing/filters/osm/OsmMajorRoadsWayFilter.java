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

package com.telenav.mesakit.map.data.formats.pbf.processing.filters.osm;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.mesakit.map.data.formats.pbf.osm.OsmHighwayTag;
import com.telenav.mesakit.map.data.formats.pbf.processing.filters.WayFilter;
import com.telenav.mesakit.map.data.formats.pbf.lexakai.DiagramPbfOsm;

/**
 * PbfFilters out highway types that we never want to consider.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPbfOsm.class)
public class OsmMajorRoadsWayFilter extends WayFilter
{
    public OsmMajorRoadsWayFilter()
    {
        super("osm-major-roads", "includes only motorway, trunk, primary and secondary ways");
        include(OsmHighwayTag.MOTORWAY);
        include(OsmHighwayTag.MOTORWAY_LINK);
        include(OsmHighwayTag.TRUNK);
        include(OsmHighwayTag.TRUNK_LINK);
        include(OsmHighwayTag.PRIMARY);
        include(OsmHighwayTag.PRIMARY_LINK);
        include(OsmHighwayTag.SECONDARY);
        include(OsmHighwayTag.SECONDARY_LINK);
    }
}
