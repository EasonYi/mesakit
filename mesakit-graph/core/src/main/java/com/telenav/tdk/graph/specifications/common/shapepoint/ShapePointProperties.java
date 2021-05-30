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

package com.telenav.tdk.graph.specifications.common.shapepoint;

import com.telenav.tdk.core.kernel.language.object.Lazy;
import com.telenav.tdk.graph.ShapePoint;
import com.telenav.tdk.graph.specifications.common.CommonDataSpecification;
import com.telenav.tdk.graph.specifications.common.element.GraphElementProperties;
import com.telenav.tdk.graph.specifications.library.attributes.Attribute;

public class ShapePointProperties extends GraphElementProperties<ShapePoint>
{
    private static final Lazy<ShapePointProperties> singleton = new Lazy<>(ShapePointProperties::new);

    public static ShapePointProperties get()
    {
        return singleton.get();
    }

    public abstract class ShapePointProperty extends com.telenav.tdk.graph.specifications.library.properties.GraphElementProperty<ShapePoint>
    {
        protected ShapePointProperty(final String name, final Attribute<?> attribute)
        {
            super(name, attribute, CommonDataSpecification.get());
            add(this);
        }
    }

    public final ShapePointProperty LOCATION = new ShapePointProperty("location", ShapePointAttributes.get().NODE_LOCATION)
    {
        @Override
        public Object value(final ShapePoint point)
        {
            return point.location();
        }
    };

    protected ShapePointProperties()
    {
    }
}
