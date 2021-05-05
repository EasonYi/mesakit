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

package com.telenav.mesakit.map.ui.desktop.graphics.canvas;

import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;
import com.telenav.mesakit.map.geography.Latitude;
import com.telenav.mesakit.map.geography.Location;
import com.telenav.mesakit.map.geography.Longitude;
import com.telenav.mesakit.map.geography.shape.rectangle.Height;
import com.telenav.mesakit.map.geography.shape.rectangle.Rectangle;
import com.telenav.mesakit.map.geography.shape.rectangle.Size;
import com.telenav.mesakit.map.geography.shape.rectangle.Width;
import com.telenav.mesakit.map.measurements.geographic.Distance;

/**
 * Maps between drawing coordinates, as expressed by <i>Drawing*</i> classes, and map coordinates, as expressed by
 * {@link Location}, {@link Latitude}, {@link Longitude}, {@link Rectangle} {@link Distance}, {@link Width}, {@link
 * Height} and {@link Size}.
 *
 * @author jonathanl (shibo)
 */
public interface MapProjection
{
    /**
     * Assigns the given coordinate system to this map projection
     */
    void coordinateSystem(CoordinateSystem system);

    /**
     * @return The drawing area in drawing coordinates
     */
    DrawingRectangle drawingArea();

    /**
     * @return The map area as a {@link Rectangle} in latitude and longitude
     */
    Rectangle mapArea();

    /**
     * @return The given {@link Location} in the drawing area
     */
    DrawingPoint toDrawing(Location location);

    /**
     * @return The given {@link Rectangle} in the drawing area
     */
    default DrawingRectangle toDrawing(final Rectangle rectangle)
    {
        return DrawingRectangle.rectangle(
                toDrawing(rectangle.topLeft()),
                toDrawing(rectangle.bottomRight()));
    }

    /**
     * @return The given {@link Width} in the drawing area
     */
    default DrawingWidth toDrawing(final Width width)
    {
        return toDrawing(width.asSize()).width();
    }

    /**
     * @return The given {@link Distance} in the drawing area
     */
    default DrawingLength toDrawing(final Distance distance)
    {
        return toDrawing(Width.degrees(distance.asDegrees()));
    }

    /**
     * @return The given {@link Height} in the drawing area
     */
    default DrawingHeight toDrawing(final Height height)
    {
        return toDrawing(height.asSize()).height();
    }

    /**
     * @return The given {@link Size} in the drawing area
     */
    default DrawingSize toDrawing(final Size size)
    {
        // Convert width and height to a location relative to the top left,
        final var at = Location.degrees(
                90 - size.height().asDegrees(),
                -180 + size.width().asDegrees());

        // project that to coordinate space, and return it as a size.
        return toDrawing(at).minus(drawingArea().topLeft()).asSize();
    }

    /**
     * @return The given drawing area size as a map {@link Size}
     */
    default Size toMap(final DrawingSize size)
    {
        final var width = toMap(size.width());
        final var height = toMap(size.height());
        return width == null || height == null ? null : Size.of(width, height);
    }

    /**
     * @return The given drawing area height as a map {@link Height}
     */
    default Height toMap(final DrawingHeight height)
    {
        final var location = toMap(height.asCoordinate());
        return location == null ? null : location.asHeight();
    }

    /**
     * @return The given drawing area width as a map {@link Width}
     */
    default Width toMap(final DrawingWidth width)
    {
        final var location = toMap(width.asCoordinate());
        return location == null ? null : location.asWidth();
    }

    /**
     * @return The given drawing area length as a map {@link Distance}
     */
    default Distance toMap(final DrawingLength length)
    {
        return Distance.degrees(toMap(length.asWidth()).asDegrees());
    }

    /**
     * @return The given drawing space coordinate as a map {@link Location}
     */
    Location toMap(DrawingPoint point);

    /**
     * @return The given rectangle in drawing coordinates as a map {@link Rectangle}
     */
    default Rectangle toMap(final DrawingRectangle rectangle)
    {
        final var at = toMap(rectangle.at());
        final var to = toMap(rectangle.to());
        return at == null || to == null ? null : Rectangle.fromLocations(at, to);
    }
}
