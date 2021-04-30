/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * //
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */

package com.telenav.mesakit.map.ui.desktop.graphics.canvas.projections;

import com.telenav.mesakit.map.geography.Location;
import com.telenav.mesakit.map.geography.shape.rectangle.Height;
import com.telenav.mesakit.map.geography.shape.rectangle.Size;
import com.telenav.mesakit.map.geography.shape.rectangle.Width;
import com.telenav.mesakit.map.ui.desktop.tiles.SlippyTileCoordinateSystem;
import org.junit.Test;

public class MercatorDrawingSurfaceProjectionTest extends BaseCoordinateMapperTest
{
    @Test
    public void testSmall()
    {
        // 10x10 degree square
        final var width = Width.degrees(10);
        final var height = Height.degrees(10);
        final var size = Size.of(width, height);

        // Map from 100,100:200,200 drawing surface rectangle to 0,0:10,10 geographic rectangle
        final var mapArea = size.asRectangle();
        final var drawingArea = drawingRectangle(100, 100, 100, 100);
        final var projection = new SphericalMercatorMapProjection(mapArea, drawingArea);

        // Check the simple cases in the corners (the middle will be distorted by the projection)
        checkMapping(projection, point(100, 100), Location.ORIGIN.offsetBy(height));
        checkMapping(projection, point(100, 200), Location.ORIGIN);
        checkMapping(projection, point(200, 100), Location.ORIGIN.offsetBy(size));
        checkMapping(projection, point(200, 200), Location.ORIGIN.offsetBy(width));
    }

    @Test
    public void testWorld()
    {
        final var projection = new SphericalMercatorMapProjection(SlippyTileCoordinateSystem.SLIPPY_TILE_BOUNDS,
                drawingRectangle(100, 100, 100, 100));

        // The origin will not be distorted
        checkMapping(projection, point(150, 150), Location.ORIGIN);

        // Ensure simple corner cases where there is no distortion
        checkMapping(projection, point(100, 100), SlippyTileCoordinateSystem.SLIPPY_TILE_BOUNDS.topLeft());
        checkMapping(projection, point(200, 100), SlippyTileCoordinateSystem.SLIPPY_TILE_BOUNDS.topRight());
        checkMapping(projection, point(100, 200), SlippyTileCoordinateSystem.SLIPPY_TILE_BOUNDS.bottomLeft());
        checkMapping(projection, point(200, 200), SlippyTileCoordinateSystem.SLIPPY_TILE_BOUNDS.bottomRight());
    }

    @Test
    public void testZoomedIn()
    {
        // 1x1 degree square
        final var width = Width.degrees(1);
        final var height = Height.degrees(1);
        final var size = new Size(width, height);
        final var seattle = Location.degrees(47.601765, -122.332335).rectangle(size);

        // Map from 100,100:200,200 drawing surface rectangle to 47.601765,-122.332335:48.601765,-121.332335
        // geographic rectangle around downtown Seattle
        final var projection = new SphericalMercatorMapProjection(seattle, drawingRectangle(100, 100, 100, 100));

        // Ensure simple cases in the corners (the middle will be distorted by the projection)
        checkMapping(projection, point(100, 100), seattle.topLeft());
        checkMapping(projection, point(100, 200), seattle.bottomLeft());
        checkMapping(projection, point(200, 100), seattle.topRight());
        checkMapping(projection, point(200, 200), seattle.bottomRight());
    }
}
