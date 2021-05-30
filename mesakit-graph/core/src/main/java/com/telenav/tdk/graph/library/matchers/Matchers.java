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

package com.telenav.tdk.graph.library.matchers;

import com.telenav.tdk.core.kernel.interfaces.object.Matcher;
import com.telenav.tdk.core.kernel.language.matching.All;
import com.telenav.tdk.core.kernel.scalars.counts.Count;
import com.telenav.tdk.graph.Edge;
import com.telenav.tdk.graph.Graph;
import com.telenav.tdk.graph.io.load.GraphConstraints;
import com.telenav.tdk.map.geography.rectangle.Rectangle;
import com.telenav.tdk.map.road.model.RoadFunctionalClass;
import com.telenav.tdk.map.road.model.RoadType;

/**
 * Convenient {@link Matcher} objects for selecting different kinds of edges. Methods that find Edge objects can consume
 * {@link Matcher} implementations, including {@link GraphConstraints}, spatial indexing methods such as {@link
 * Graph#edgesIntersecting(Rectangle, Matcher)} and navigation methods like {@link Edge#route(Matcher)}.
 *
 * @author jonathanl (shibo)
 * @see GraphConstraints
 * @see Edge
 */
public class Matchers
{
    public static Matcher<Edge> ALL = new All<>();

    public static Matcher<Edge> AT_LEAST_PRIVATE_ROAD = moreImportantThanOrEqual(RoadType.PRIVATE_ROAD);

    public static Matcher<Edge> AT_LEAST_LOCAL_ROAD = moreImportantThanOrEqual(RoadType.LOCAL_ROAD);

    public static Matcher<Edge> AT_LEAST_THROUGHWAY = moreImportantThanOrEqual(RoadType.THROUGHWAY);

    public static Matcher<Edge> AT_LEAST_HIGHWAY = moreImportantThanOrEqual(RoadType.HIGHWAY);

    public static Matcher<Edge> AT_LEAST_FREEWAY = moreImportantThanOrEqual(RoadType.FREEWAY);

    public static Matcher<Edge> AT_LEAST_FRONTAGE_ROAD = moreImportantThanOrEqual(RoadType.FRONTAGE_ROAD);

    public static Matcher<Edge> AT_LEAST_THROUGHWAY_MAIN_ROAD = mainRoadMoreImportantThanOrEqual(RoadType.THROUGHWAY);

    public static Matcher<Edge> AT_LEAST_HIGHWAY_MAIN_ROAD = mainRoadMoreImportantThanOrEqual(RoadType.HIGHWAY);

    public static Matcher<Edge> AT_LEAST_FREEWAY_MAIN_ROAD = mainRoadMoreImportantThanOrEqual(RoadType.FREEWAY);

    public static final Matcher<Edge> CONNECTORS = Edge::isConnector;

    public static Matcher<Edge> FREEWAYS = edge -> edge.roadType() == RoadType.FREEWAY;

    public static final Matcher<Edge> FREEWAYS_WITHOUT_RAMPS = edge -> edge.roadType().equals(RoadType.FREEWAY) && !edge.isRamp() && !edge.isJunctionEdge();

    public static final Matcher<Edge> LINKS = Edge::isLink;

    public static final Matcher<Edge> RAMPS = Edge::isRamp;

    public static final Matcher<Edge> SHAPELESS = value -> !value.isShaped();

    public static final Matcher<Edge> SIGNIFICANT_ROADS = edge ->
    {
        if (edge.roadType().equals(RoadType.THROUGHWAY)
                && (edge.laneCount() == null || edge.laneCount().isLessThan(Count._2)))
        {
            return false;
        }
        return edge.roadType().isMoreImportantThan(RoadType.LOCAL_ROAD);
    };

    public static Matcher<Edge> lessImportantThan(final RoadFunctionalClass functionalClass)
    {
        return edge -> edge.roadFunctionalClass().isLessImportantThan(functionalClass);
    }

    public static Matcher<Edge> lessImportantThanOrEqual(final RoadFunctionalClass functionalClass)
    {
        return edge -> edge.roadFunctionalClass().isLessImportantThanOrEqual(functionalClass);
    }

    public static Matcher<Edge> mainRoadMoreImportantThanOrEqual(final RoadType type)
    {
        return edge -> edge.isMainRoad() && edge.roadType().isEqualOrMoreImportantThan(type);
    }

    public static Matcher<Edge> moreImportantThan(final RoadFunctionalClass functionalClass)
    {
        return edge -> edge.roadFunctionalClass().isMoreImportantThan(functionalClass);
    }

    public static Matcher<Edge> moreImportantThan(final RoadType type)
    {
        return edge -> edge.roadType().isMoreImportantThan(type);
    }

    public static Matcher<Edge> moreImportantThanOrEqual(final RoadFunctionalClass functionalClass)
    {
        return edge -> edge.roadFunctionalClass().isMoreImportantThanOrEqual(functionalClass);
    }

    public static Matcher<Edge> moreImportantThanOrEqual(final RoadType type)
    {
        return edge -> edge.roadType().isEqualOrMoreImportantThan(type);
    }
}
