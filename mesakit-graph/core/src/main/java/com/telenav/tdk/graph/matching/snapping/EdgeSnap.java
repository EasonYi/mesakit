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

package com.telenav.tdk.graph.matching.snapping;

import com.telenav.tdk.graph.Edge;
import com.telenav.tdk.map.geography.polyline.PolylineSnap;

public class EdgeSnap extends PolylineSnap
{
    private final Edge edge;

    public EdgeSnap(final Edge edge, final PolylineSnap snap)
    {
        super(snap.latitude(), snap.longitude(), snap.target(), snap.offsetOnSegment(), snap.source(),
                snap.polylineIndex());

        this.edge = edge;
    }

    public Edge edge()
    {
        return edge;
    }

    @Override
    public String toString()
    {
        return edge.identifier().toString();
    }
}
