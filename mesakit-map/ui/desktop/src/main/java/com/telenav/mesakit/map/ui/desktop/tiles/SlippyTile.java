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

package com.telenav.mesakit.map.ui.desktop.tiles;

import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.ui.desktop.graphics.drawing.drawables.Label;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;
import com.telenav.mesakit.map.geography.Location;
import com.telenav.mesakit.map.geography.shape.rectangle.Rectangle;
import com.telenav.mesakit.map.geography.shape.rectangle.Size;
import com.telenav.mesakit.map.ui.desktop.graphics.canvas.MapCanvas;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * A "slippy tile" is a rectangle defined by a zoom level and x, y coordinates in the grid of all tiles. You can get the
 * bounds of a particular slippy tile like this:
 *
 * <pre>
 * var bounds = new SlippyTile()
 *     .withX(300)
 *     .withY(40)
 *     .withZoomLevel(ZoomLevel.forInteger(5))
 *     .bounds()
 * </pre>
 *
 * <p>
 * You can also find the slippy tile for a location like this:
 * </p>
 *
 * <pre>
 * var tile = new SlippyTile(CheckType.OSM, zoom, location)
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see "http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames"
 */
public class SlippyTile
{
    /**
     * Width and height of standard slippy tiles
     */
    public static final Dimension STANDARD_TILE_SIZE = new Dimension(256, 256);

    /**
     * @return The smallest tile that is larger than the given size
     */
    public static SlippyTile largerThan(final Size size)
    {
        var tile = ZoomLevel.CLOSEST.tileAt(Location.ORIGIN);
        while (!tile.size().isGreaterThan(size) && !tile.getZoomLevel().isFurthestOut())
        {
            tile = tile.parent();
        }
        return tile;
    }

    public static SlippyTile tile()
    {
        return new SlippyTile(null, 0, 0);
    }

    private int x;

    private int y;

    private ZoomLevel zoom;

    protected SlippyTile(final ZoomLevel zoom, final int x, final int y)
    {
        this.zoom = zoom;
        if (x < 0 || x >= zoom.widthInTiles())
        {
            throw new IllegalArgumentException("Invalid x = " + x);
        }
        if (y < 0 || y >= zoom.heightInTiles())
        {
            throw new IllegalArgumentException("Invalid y = " + y);
        }
        this.x = x;
        this.y = y;
    }

    private SlippyTile(final SlippyTile that)
    {
        x = that.x;
        y = that.y;
        zoom = that.zoom;
    }

    public FileName asFileName()
    {
        return FileName.parse(x + "-" + y + "-" + zoom.level() + ".png");
    }

    @SuppressWarnings({ "SameReturnValue" })
    public Dimension dimension()
    {
        return STANDARD_TILE_SIZE;
    }

    /**
     * Draws the outline of this slippy tile on the given canvas in the given style clipped to the given bounds
     */
    public void drawOutline(final MapCanvas canvas,
                            final Style style,
                            final Rectangle mapBounds)
    {
        // Draw tile grid rectangle for this tile
        final var tileBounds = tileBounds();
        final var bottomLeft = canvas.toDrawingUnits(tileBounds.bottomLeft());
        final var bottomRight = canvas.toDrawingUnits(tileBounds.bottomRight());
        final var topRight = canvas.toDrawingUnits(tileBounds.topRight());
        canvas.drawLine(style, bottomLeft, bottomRight);
        canvas.drawLine(style, topRight, bottomRight);

        // Draw label for tile rectangle
        final var visible = mapBounds.intersect(tileBounds);
        if (visible != null)
        {
            final var text = toString();
            final var topLeft = canvas.toDrawingUnits(visible.topLeft()).plus(10, 10);
            Label.label(style)
                    .at(canvas.toCoordinates(topLeft))
                    .withText(text)
                    .draw(canvas);
        }
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof SlippyTile)
        {
            final var that = (SlippyTile) object;
            return x == that.x && y == that.y && zoom.equals(that.zoom);
        }
        return false;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public ZoomLevel getZoomLevel()
    {
        return zoom;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(x, y, zoom);
    }

    public SlippyTile parent()
    {
        return getZoomLevel().zoomOut().tileAt(tileBounds().center());
    }

    public Size size()
    {
        return tileBounds().size();
    }

    public Rectangle tileBounds()
    {
        return new SlippyTileCoordinateSystem(zoom).bounds(this);
    }

    public List<SlippyTile> tilesInside()
    {
        final List<SlippyTile> tiles = new ArrayList<>();
        final var zoomedIn = getZoomLevel().zoomIn();
        final var x = this.x * 2;
        final var y = this.y * 2;
        tiles.add(new SlippyTile(zoomedIn, x, y));
        tiles.add(new SlippyTile(zoomedIn, x + 1, y));
        tiles.add(new SlippyTile(zoomedIn, x, y + 1));
        tiles.add(new SlippyTile(zoomedIn, x + 1, y + 1));
        return tiles;
    }

    @Override
    public String toString()
    {
        return "x = " + x + ", y = " + y + ", z = " + zoom.level();
    }

    public SlippyTile withX(final int x)
    {
        final var tile = new SlippyTile(this);
        tile.x = x;
        return tile;
    }

    public SlippyTile withY(final int y)
    {
        final var tile = new SlippyTile(this);
        tile.y = y;
        return tile;
    }

    public SlippyTile withZoomLevel(final ZoomLevel zoom)
    {
        final var tile = new SlippyTile(this);
        tile.zoom = zoom;
        return tile;
    }
}
