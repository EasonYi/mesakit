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

package com.telenav.mesakit.map.ui.desktop.graphics.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;
import com.telenav.kivakit.ui.desktop.graphics.drawing.surfaces.java2d.Java2dShapes;
import com.telenav.mesakit.map.geography.Location;
import com.telenav.mesakit.map.geography.shape.rectangle.Rectangle;
import com.telenav.mesakit.map.measurements.geographic.Distance;
import com.telenav.mesakit.map.ui.desktop.graphics.canvas.MapCanvas;

import java.awt.Shape;

import static com.telenav.kivakit.ensure.Ensure.unsupported;

/**
 * @author jonathanl (shibo)
 */
public class MapLine extends LabeledMapShape
{
    public static MapLine line()
    {
        return line(null);
    }

    public static MapLine line(Style style)
    {
        return new MapLine(style, null, null, null);
    }

    public static MapLine line(Style style, Location from, Location to, String label)
    {
        return new MapLine(style, from, to, label);
    }

    private Drawable fromArrowHead;

    private Drawable toArrowHead;

    private Location to;

    private final String label;

    protected MapLine(Style style, Location at, Location to, String label)
    {
        super(style, at, label);

        this.to = to;
        this.label = label;
    }

    protected MapLine(MapLine that)
    {
        super(that);

        to = that.to;
        label = that.label;
        fromArrowHead = that.fromArrowHead;
        toArrowHead = that.toArrowHead;
    }

    @Override
    public Rectangle bounds()
    {
        return Rectangle.fromLocations(from(), to());
    }

    @Override
    public MapLine copy()
    {
        return new MapLine(this);
    }

    public Location from()
    {
        return location();
    }

    @Override
    public Shape onDraw(MapCanvas canvas)
    {
        var line = line(style(), from(), to, label)
                .withFromArrowHead(fromArrowHead)
                .withToArrowHead(toArrowHead)
                .withLocation(canvas.toDrawing(location()))
                .draw(canvas);

        return Java2dShapes.combine(line, super.onDraw(canvas));
    }

    @Override
    public MapLine scaledBy(double scaleFactor)
    {
        return unsupported();
    }

    public Location to()
    {
        return to;
    }

    @Override
    public MapLine withColors(Style style)
    {
        return (MapLine) super.withColors(style);
    }

    @Override
    public MapLine withDrawColor(Color color)
    {
        return (MapLine) super.withDrawColor(color);
    }

    @Override
    public MapLine withDrawStrokeWidth(Distance width)
    {
        return (MapLine) super.withDrawStrokeWidth(width);
    }

    @Override
    public MapLine withDrawStrokeWidth(DrawingWidth width)
    {
        return (MapLine) super.withDrawStrokeWidth(width);
    }

    @Override
    public MapLine withFillColor(Color color)
    {
        return (MapLine) super.withFillColor(color);
    }

    @Override
    public MapLine withFillStroke(Stroke stroke)
    {
        return (MapLine) super.withFillStroke(stroke);
    }

    @Override
    public MapLine withFillStrokeWidth(Distance width)
    {
        return (MapLine) super.withFillStrokeWidth(width);
    }

    @Override
    public MapLine withFillStrokeWidth(DrawingWidth width)
    {
        return (MapLine) super.withFillStrokeWidth(width);
    }

    public MapLine withFrom(Location from)
    {
        return withLocation(from);
    }

    public MapLine withFromArrowHead(Drawable arrowHead)
    {
        var copy = copy();
        copy.fromArrowHead = arrowHead;
        return copy;
    }

    @Override
    public MapLine withLabelText(String label)
    {
        return (MapLine) super.withLabelText(label);
    }

    @Override
    public MapLine withLocation(DrawingPoint at)
    {
        return (MapLine) super.withLocation(at);
    }

    @Override
    public MapLine withLocation(Location at)
    {
        return (MapLine) super.withLocation(at);
    }

    @Override
    public MapLine withMargin(int margin)
    {
        return (MapLine) super.withMargin(margin);
    }

    @Override
    public MapLine withOffset(int dx, int dy)
    {
        return (MapLine) super.withOffset(dx, dy);
    }

    @Override
    public MapLine withRoundedLabelCorners(DrawingLength corner)
    {
        return (MapLine) super.withRoundedLabelCorners(corner);
    }

    @Override
    public MapLine withRoundedLabelCorners(DrawingWidth cornerWidth,
                                           DrawingHeight cornerHeight)
    {
        return (MapLine) super.withRoundedLabelCorners(cornerWidth, cornerHeight);
    }

    @Override
    public MapLine withStyle(Style style)
    {
        return (MapLine) super.withStyle(style);
    }

    @Override
    public MapLine withTextColor(Color color)
    {
        return (MapLine) super.withTextColor(color);
    }

    public MapLine withTo(Location to)
    {
        var copy = copy();
        this.to = to;
        return copy;
    }

    public MapLine withToArrowHead(Drawable arrowHead)
    {
        var copy = copy();
        copy.toArrowHead = arrowHead;
        return copy;
    }
}
