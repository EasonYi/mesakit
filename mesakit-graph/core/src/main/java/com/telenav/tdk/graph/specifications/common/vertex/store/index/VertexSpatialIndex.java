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

package com.telenav.tdk.graph.specifications.common.vertex.store.index;

import com.telenav.tdk.core.kernel.scalars.counts.Count;
import com.telenav.tdk.graph.Graph;
import com.telenav.tdk.graph.Vertex;
import com.telenav.tdk.graph.identifiers.VertexIdentifier;
import com.telenav.tdk.graph.specifications.common.element.store.index.GraphElementSpatialIndex;

public class VertexSpatialIndex extends GraphElementSpatialIndex<Vertex>
{
    public VertexSpatialIndex(final Graph graph, final Count maximumObjectsPerQuadrant)
    {
        super(graph, maximumObjectsPerQuadrant.asInt());
    }

    @Override
    protected Vertex forIdentifier(final long identifier)
    {
        return graph().vertexForIdentifier(new VertexIdentifier((int) identifier));
    }
}
