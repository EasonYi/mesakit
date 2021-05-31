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

package com.telenav.tdk.navigation.routing.cost;

import com.telenav.kivakit.kernel.scalars.levels.Weight;
import com.telenav.kivakit.navigation.routing.cost.operators.MaximumCost;
import com.telenav.kivakit.navigation.routing.cost.operators.Minimum;
import com.telenav.kivakit.navigation.routing.cost.operators.Plus;
import com.telenav.kivakit.navigation.routing.cost.operators.Weighted;
import com.telenav.mesakit.graph.Edge;

/**
 * Cost function for determining the expense of traversing an edge.
 *
 * @author jonathanl (shibo)
 */
public interface CostFunction
{
    /**
     * @return The cost of traveling along the given edge
     */
    Cost cost(Edge edge);

    /**
     * @return A cost function that returns the maximum of this cost function and that cost function
     */
    default CostFunction maximum(final CostFunction that)
    {
        return new MaximumCost(this, that);
    }

    /**
     * @return A cost function that returns the minimum of this cost function and that cost function
     */
    default CostFunction minimum(final CostFunction that)
    {
        return new Minimum(this, that);
    }

    /**
     * @return A cost function that adds this cost function to the given cost function
     */
    default CostFunction plus(final CostFunction that)
    {
        return new Plus(this, that);
    }

    /**
     * @return This cost function multiplied by the given weight
     */
    default CostFunction weighted(final Weight weight)
    {
        return new Weighted(this, weight);
    }

    /**
     * @return This cost function plus the given cost function at the given weight
     */
    default CostFunction weightedSum(final Weight weight, final CostFunction that)
    {
        return weighted(weight.inverse()).plus(that.weighted(weight));
    }
}
