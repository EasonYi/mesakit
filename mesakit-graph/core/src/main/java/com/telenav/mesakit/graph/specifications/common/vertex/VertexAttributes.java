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

package com.telenav.mesakit.graph.specifications.common.vertex;

import com.telenav.kivakit.core.object.Lazy;
import com.telenav.mesakit.graph.metadata.DataSpecification;
import com.telenav.mesakit.graph.specifications.common.CommonDataSpecification;
import com.telenav.mesakit.graph.specifications.common.node.NodeAttributes;

public class VertexAttributes extends NodeAttributes
{
    // The attributes in this class are shared (from the common data specification) so they need to have the
    // same identifiers in all subclasses. See the superclass of this class for details.

    private static final Lazy<VertexAttributes> singleton = Lazy.of(VertexAttributes::new);

    public static VertexAttributes get()
    {
        return singleton.get();
    }

    public class VertexAttribute extends NodeAttribute
    {
        public VertexAttribute(String name)
        {
            super(name);
        }
    }

    public final VertexAttribute ALL_EDGE_COUNT = new VertexAttribute("BOTH_EDGE_COUNT");

    public final VertexAttribute IN_EDGE_COUNT = new VertexAttribute("IN_EDGE_COUNT");

    public final VertexAttribute IS_CLIPPED = new VertexAttribute("IS_CLIPPED");

    public final VertexAttribute IS_SYNTHETIC = new VertexAttribute("IS_SYNTHETIC");

    public final VertexAttribute OUT_EDGE_COUNT = new VertexAttribute("OUT_EDGE_COUNT");

    public final VertexAttribute GRADE_SEPARATION = new VertexAttribute("GRADE_SEPARATION");

    protected VertexAttributes()
    {
    }

    @Override
    protected DataSpecification dataSpecification()
    {
        return CommonDataSpecification.get();
    }
}
