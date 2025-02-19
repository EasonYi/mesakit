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

package com.telenav.mesakit.map.geography.shape.rectangle;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.mesakit.map.geography.lexakai.DiagramRectangle;

/**
 * Interface for an object that can test if it intersects a rectangle.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramRectangle.class)
public interface Intersectable
{
    /**
     * @return True if this object intersects or is completely contained by the given rectangle, false if the object
     * lies outside the rectangle and does not intersect it.
     */
    boolean intersects(Rectangle rectangle);
}
