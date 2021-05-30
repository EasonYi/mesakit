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

package com.telenav.tdk.graph.matching.conflation;

import com.telenav.tdk.core.kernel.logging.*;
import com.telenav.tdk.core.kernel.scalars.levels.Percentage;
import com.telenav.tdk.graph.*;
import com.telenav.tdk.graph.ui.viewer.GraphDebugViewer;
import com.telenav.tdk.map.measurements.*;
import com.telenav.tdk.map.road.model.RoadFunctionalClass;

public class EdgeConflater
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * When the enhancer encounters a candidate edge matching this identifier, it will execute the println statement in
     * the conflate() method below, where a debug breakpoint can easily be set.
     */
    private static final long DEBUG_CANDIDATE_EDGE_IDENTIFIER = Long.MIN_VALUE;

    /**
     * When the enhancer encounters an edge whose name contains this string (case independent), it will execute the
     * println statement in the conflate() method below, where a debug breakpoint can easily be set.
     */
    private static final String DEBUG_CANDIDATE_ROAD_NAME = "<<none>>";

    /**
     * True to enable visual debugger
     */
    private static final boolean DEBUG = false;

    /**
     * The base graph being conflated with
     */
    private final Graph base;

    /**
     * Viewer
     */
    private final GraphDebugViewer viewer;

    /**
     * The minimum spacing of points in an augmented polyline
     */
    private Distance augmentationSpacing = Distance.meters(5);

    /**
     * True to conflate edges that have different names
     */
    private boolean conflateEdgesWithDifferentNames;

    /**
     * True if conflation to ways labeled as 'construction' or 'proposed' should occur
     */
    private boolean conflateToConstruction;

    /**
     * Maximum distance apart for a point to be considered "close" to a polyline
     */
    private Distance maximumSnapDistance = Distance.meters(10);

    /**
     * Maximum amount heading can deviate when looking for close points during conflation
     */
    private Angle maximumHeadingDeviation = Angle.degrees(15);

    /**
     * The minimum closeness for an edge to conflate
     */
    private Percentage minimumCloseness = new Percentage(15);

    /**
     * Construct
     *
     * @param base The base graph to conflate with
     */
    public EdgeConflater(final Graph base)
    {
        this.base = base;
        viewer = new GraphDebugViewer(DEBUG);
    }

    public void augmentationSpacing(final Distance spacing)
    {
        augmentationSpacing = spacing;
    }

    /**
     * @return The set of conflations in the base map for the given enhancing edge.
     */
    public ConflationSet conflate(final Edge enhancing)
    {
        // Show current edge in viewer
        viewer.clear();
        viewer.current(enhancing, "enhancing " + enhancing.identifierAsLong());

        // Set of conflations
        final var conflations = new ConflationSet();

        // Augment road shape
        final var augmented = enhancing.roadShape().augmented(augmentationSpacing);

        // Go through all the edges that could possibly match in the base graph
        for (final var base : base.edgesIntersecting(enhancing.bounds().expanded(maximumSnapDistance)))
        {
            // If we hit the debug edge identifier or road name defined at the top of this file,
            if (base.identifier().asLong() == DEBUG_CANDIDATE_EDGE_IDENTIFIER || (base.roadName() != null
                    && base.safeRoadName().toLowerCase().contains(DEBUG_CANDIDATE_ROAD_NAME.toLowerCase())))
            {
                // break at this line in the debugger (for convenience)
                LOGGER.information("Base edge " + base + " (" + base.displayRoadName() + ") reached");
            }

            // We don't want to conflate a truly important enhancing edge (main, 1st or 2nd class)
            // with a far less important edge like a footpath (4th class).
            if (!shouldConflate(enhancing, base))
            {
                continue;
            }

            // If we don't want to conflate edges with different names
            if (!conflateEdgesWithDifferentNames)
            {
                // and both edges are named, but their names differ
                if (base.roadName() != null && enhancing.roadName() != null
                        && !base.hasSameStandardizedBaseNameAs(enhancing))
                {
                    // then skip it
                    continue;
                }
            }

            // Augment the candidate
            final var augmentedCandidate = base.roadShape().augmented(augmentationSpacing);

            // Determine closeness
            final Percentage closeness;
            final Distance overlap;
            if (augmented.length().isLessThan(augmentedCandidate.length()))
            {
                closeness = augmented.closeness(augmentedCandidate, maximumSnapDistance, maximumHeadingDeviation);
                overlap = augmented.length().scaledBy(closeness);
            }
            else
            {
                closeness = augmentedCandidate.closeness(augmented, maximumSnapDistance, maximumHeadingDeviation);
                overlap = augmentedCandidate.length().scaledBy(closeness);
            }

            // Show candidate edge in viewer
            viewer.candidate(base, "candidate (" + closeness + ")");
            viewer.frameComplete();

            // If the candidate edge is close enough
            if (closeness.isGreaterThan(minimumCloseness))
            {
                // it is a valid conflation, so add it to the set of conflations
                conflations.add(new Conflation(enhancing, base.forward(), closeness, overlap));
            }
        }

        // If there was at least one conflation,
        if (!conflations.isEmpty())
        {
            // show the best result in the viewer
            viewer.clearCandidateEdge();
            viewer.highlight(conflations.closest().base(), "closest (" + conflations.closest().closeness() + ")");
            viewer.frameComplete();
        }

        // Return any conflations we found
        return conflations;
    }

    public void conflateEdgesWithDifferentNames(final boolean conflateEdgesWithDifferentNames)
    {
        this.conflateEdgesWithDifferentNames = conflateEdgesWithDifferentNames;
    }

    public void conflateToConstruction(final boolean conflateToConstruction)
    {
        this.conflateToConstruction = conflateToConstruction;
    }

    public void maximumHeadingDeviation(final Angle maximumHeadingDeviation)
    {
        this.maximumHeadingDeviation = maximumHeadingDeviation;
    }

    public void maximumSnapDistance(final Distance maximumSnapDistance)
    {
        this.maximumSnapDistance = maximumSnapDistance;
    }

    public void minimumCloseness(final Percentage minimumCloseness)
    {
        this.minimumCloseness = minimumCloseness;
    }

    /**
     * @return True if the given edge should be conflated to the base graph candidate edge
     */
    private boolean shouldConflate(final Edge edge, final Edge baseCandidate)
    {
        // If we don't want to conflate edges with different names
        if (!conflateEdgesWithDifferentNames)
        {
            // and both edges are named, but their names differ
            if (baseCandidate.roadName() != null && edge.roadName() != null
                    && !baseCandidate.hasSameStandardizedBaseNameAs(edge))
            {
                // then don't try to conflate
                return false;
            }
        }

        // If the base candidate is fourth class,
        if (baseCandidate.roadFunctionalClass() == RoadFunctionalClass.FOURTH_CLASS)
        {
            // and the edge is much higher class,
            if (edge.roadFunctionalClass() == RoadFunctionalClass.FIRST_CLASS
                    || edge.roadFunctionalClass() == RoadFunctionalClass.SECOND_CLASS)
            {
                // then don't conflate to the low class road
                return false;
            }
        }

        switch (baseCandidate.roadType())
        {
            case WALKWAY:
            case NON_NAVIGABLE:

                // If the base edge is construction or proposed
                if (baseCandidate.isUnderConstruction() || baseCandidate.osmIsProposed())
                {
                    // conflate to the edge if the caller desired it
                    return conflateToConstruction;
                }
                return false;

            default:
                return true;
        }
    }
}
