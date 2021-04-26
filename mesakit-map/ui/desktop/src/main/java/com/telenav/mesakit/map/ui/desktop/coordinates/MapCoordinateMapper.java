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

package com.telenav.mesakit.map.ui.desktop.coordinates;

import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingPoint;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingRectangle;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSize;
import com.telenav.kivakit.ui.swing.graphics.drawing.DrawingSurface;
import com.telenav.mesakit.map.geography.Location;
import com.telenav.mesakit.map.geography.shape.rectangle.Rectangle;
import com.telenav.mesakit.map.geography.shape.rectangle.Size;

/**
 * Maps between {@link DrawingSurface} coordinates and map coordinates, as expressed by {@link Location}.
 *
 * @author jonathanl (shibo)
 */
public interface MapCoordinateMapper
{
    DrawingPoint toDrawingPoint(Location location);

    default DrawingRectangle toDrawingRectangle(final Rectangle rectangle)
    {
        return DrawingRectangle.rectangle(
                toDrawingPoint(rectangle.topLeft()),
                toDrawingSize(rectangle.size()));
    }

    default DrawingSize toDrawingSize(final Size size)
    {
        final var mapped = toDrawingPoint(size.asLocation());
        return DrawingSize.size(mapped.x(), mapped.y());
    }

    Location toMapLocation(DrawingPoint point);

    default Rectangle toMapRectangle(final DrawingRectangle rectangle)
    {
        return Rectangle.fromLocations(
                toMapLocation(rectangle.at()),
                toMapLocation(rectangle.to()));
    }
}
