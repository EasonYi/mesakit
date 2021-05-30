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

package com.telenav.tdk.navigation.routing.bidijkstra;

import com.telenav.tdk.core.kernel.scalars.counts.Count;
import com.telenav.tdk.graph.Vertex;
import com.telenav.tdk.map.measurements.Distance;
import com.telenav.tdk.navigation.routing.RoutingDebugger;
import com.telenav.tdk.navigation.routing.RoutingLimiter;
import com.telenav.tdk.navigation.routing.RoutingRequest;
import com.telenav.tdk.navigation.routing.cost.CostFunction;

public class BiDijkstraRoutingRequest extends RoutingRequest
{
    /** The number of meets that should be found */
    private Count meets;

    /** Maximum multiple of the first meet for future meets before search stops */
    private final double maximumFirstMeetCostMultiple = 1.5;

    /** The forward heuristic cost function */
    private CostFunction forwardHeuristicCostFunction;

    /** The backward heuristic cost function */
    private CostFunction backwardHeuristicCostFunction;

    public BiDijkstraRoutingRequest(final Vertex start, final Vertex end)
    {
        super(start, end);
    }

    protected BiDijkstraRoutingRequest(final BiDijkstraRoutingRequest that, final RoutingLimiter limiter,
                                       final RoutingDebugger debugger)
    {
        super(that, limiter, debugger);
        this.meets = that.meets;
        this.forwardHeuristicCostFunction = that.forwardHeuristicCostFunction;
        this.backwardHeuristicCostFunction = that.backwardHeuristicCostFunction;
    }

    public CostFunction backwardHeuristicCostFunction()
    {
        return this.backwardHeuristicCostFunction;
    }

    @Override
    public String description()
    {
        return "BiDijkstra";
    }

    public CostFunction forwardHeuristicCostFunction()
    {
        return this.forwardHeuristicCostFunction;
    }

    public double maximumFirstMeetCostMultiple()
    {
        return this.maximumFirstMeetCostMultiple;
    }

    public Count meets()
    {
        if (this.meets == null)
        {
            final var distance = distance();
            if (distance.isLessThan(Distance.miles(50)))
            {
                this.meets = Count.of(200);
            }
            else if (distance.isLessThan(Distance.miles(300)))
            {
                this.meets = Count.of(500);
            }
            else
            {
                this.meets = Count.of(1_000);
            }
        }
        return this.meets;
    }

    public BiDijkstraRoutingRequest withBackwardHeuristicCostFunction(final CostFunction backwardHeuristicCostFunction)
    {
        if (backwardHeuristicCostFunction != null)
        {
            final var request = new BiDijkstraRoutingRequest(this, null, null);
            request.backwardHeuristicCostFunction = backwardHeuristicCostFunction;
            return request;
        }
        return this;
    }

    public BiDijkstraRoutingRequest withDebugger(final RoutingDebugger debugger)
    {
        return new BiDijkstraRoutingRequest(this, null, debugger);
    }

    public BiDijkstraRoutingRequest withForwardHeuristicCostFunction(final CostFunction forwardHeuristicCostFunction)
    {
        if (forwardHeuristicCostFunction != null)
        {
            final var router = new BiDijkstraRoutingRequest(this, null, null);
            router.forwardHeuristicCostFunction = forwardHeuristicCostFunction;
            return router;
        }
        return this;
    }

    public BiDijkstraRoutingRequest withLimiter(final RoutingLimiter limiter)
    {
        return new BiDijkstraRoutingRequest(this, limiter, null);
    }

    public BiDijkstraRoutingRequest withMaximumFirstMeetCostMultiple(final double maximumFirstMeetCostMultiple)
    {
        final var router = new BiDijkstraRoutingRequest(this, null, null);
        router.maximumFirstMeetCostMultiple = this.maximumFirstMeetCostMultiple;
        return router;
    }

    public BiDijkstraRoutingRequest withMeets(final Count meets)
    {
        final var router = new BiDijkstraRoutingRequest(this, null, null);
        router.meets = meets;
        return router;
    }
}
