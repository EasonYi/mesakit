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

package com.telenav.mesakit.graph.io.load.loaders.region.regions;

import com.telenav.mesakit.graph.Graph;
import com.telenav.mesakit.graph.Place;
import com.telenav.mesakit.graph.collections.EdgeSequence;
import com.telenav.mesakit.graph.io.load.loaders.region.RegionalGraphLoader;
import com.telenav.mesakit.map.region.regions.MetropolitanArea;

public class MetropolitanAreaLoader extends RegionalGraphLoader
{
    private final Graph source;

    private final MetropolitanArea metropolitanArea;

    public MetropolitanAreaLoader(Graph source, MetropolitanArea metropolitanArea)
    {
        this.source = source;
        this.metropolitanArea = metropolitanArea;
    }

    @Override
    protected EdgeSequence forwardEdges()
    {
        return source.edgesIntersecting(metropolitanArea.bounds(), edge -> !edge.isReverse()
                && metropolitanArea.equals(edge.metropolitanArea()));
    }

    @Override
    protected Iterable<Place> places()
    {
        return source.placesInside(metropolitanArea);
    }

    @Override
    protected Graph sourceGraph()
    {
        return source;
    }
}
