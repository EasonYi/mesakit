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

package com.telenav.tdk.graph.library.osm.change;

import com.telenav.tdk.map.geography.Location;
import com.telenav.tdk.map.geography.polyline.Polyline;
import com.telenav.tdk.map.geography.polyline.PolylineBuilder;
import com.telenav.tdk.map.measurements.Distance;

/**
 * Models a connection to a modified way. There are two types of connections, one where a new node is added to connect a
 * given polyline segment (an "under-shoot") and one where the last node of the polyline segment is instead moved to
 * connect to the modified way (an "overshoot").
 *
 * @author jonathanl (shibo)
 */
public class Connection
{
    public enum End
    {
        FROM,
        TO
    }

    public enum Type
    {
        /**
         * The connection should be made by adding a new node
         */
        UNDERSHOOT,

        /**
         * The connection should be made by moving the last node
         */
        OVERSHOOT,

        /**
         * The connection should be made by deleting the end point node and moving the one before it
         */
        TWO_SEGMENT_OVERSHOOT
    }

    private final Location location;

    private final Connection.Type type;

    Connection(final Location location, final Connection.Type type)
    {
        this.location = location;
        this.type = type;
    }

    public Location location()
    {
        return location;
    }

    public Connection.Type type()
    {
        return type;
    }

    /**
     * Connect the 'from' end of the given shape to the connection location
     */
    public Polyline withFromEndConnected(final Polyline shape)
    {
        // The new polyline
        final var builder = new PolylineBuilder();

        // Get the location to connect to
        final var location = location();

        switch (type())
        {
            case TWO_SEGMENT_OVERSHOOT:
                if (!shape.isSegment())
                {
                    // Add shape except start point, then overwrite first location
                    builder.addAllButFirst(shape.locationSequence());
                    builder.set(0, location);
                }
                break;

            case OVERSHOOT:
            case UNDERSHOOT:

                // If we don't already contain the location and the snap is not too far
                if (!shape.has(location) && shape.start().isClose(location, Distance.meters(5)))
                {
                    // snap the start of the shape to the location
                    builder.add(location);
                    builder.addAllButFirst(shape.locationSequence());
                }
                break;
        }

        // The modified polyline
        return builder.isValid() ? builder.build() : null;
    }

    /**
     * Connect the 'to' end of the given shape to the connection location
     */
    public Polyline withToEndConnected(final Polyline shape)
    {
        // The new polyline
        final var builder = new PolylineBuilder();

        // Get the location to connect to
        final var location = location();

        switch (type())
        {
            case TWO_SEGMENT_OVERSHOOT:
                if (!shape.isSegment())
                {
                    // Add all but the last point, then overwrite the last location
                    builder.addAllButLast(shape.locationSequence());
                    builder.set(shape.size() - 2, location);
                }
                break;

            case OVERSHOOT:
            case UNDERSHOOT:

                // If we don't already contain the location
                if (!shape.has(location) && shape.end().isClose(location, Distance.meters(5)))
                {
                    // snap the end of the shape to the location
                    builder.addAllButLast(shape.locationSequence());
                    builder.add(location);
                }
                break;
        }

        // Return the new polyline
        return builder.isValid() ? builder.build() : null;
    }
}
