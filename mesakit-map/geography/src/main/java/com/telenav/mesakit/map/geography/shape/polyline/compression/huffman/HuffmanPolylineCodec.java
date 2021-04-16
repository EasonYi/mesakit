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

package com.telenav.mesakit.map.geography.shape.polyline.compression.huffman;

import com.telenav.mesakit.map.geography.Location;
import com.telenav.mesakit.map.geography.shape.polyline.Polyline;
import com.telenav.kivakit.core.collections.primitive.array.scalars.ByteArray;
import com.telenav.kivakit.core.collections.primitive.list.ByteList;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.resource.resources.other.PropertyMap;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;

import java.util.Arrays;

import static com.telenav.kivakit.data.compression.SymbolConsumer.Directive.CONTINUE;
import static com.telenav.kivakit.data.compression.SymbolConsumer.Directive.STOP;

/**
 * <b>Work in Progress</b>
 * <p>
 *
 * @author jonathanl (shibo)
 */
public class HuffmanPolylineCodec
{
    public static final int END_OF_OFFSETS = 0;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    private final HuffmanCodec<Integer> codec;

    private int[] offsetsInDm5;

    private int index;

    private int destination;

    private int lastInDm5;

    public HuffmanPolylineCodec()
    {
        final var symbols = Symbols.load(PropertyMap.load(getClass(), "polyline.codec"),
                new IntegerConverter(LOGGER));

        codec = HuffmanCodec.from(symbols, Maximum._32);

        initialize();
    }

    public long[] decode(final ByteList input)
    {
        // Reset the cursor to the start,
        input.reset();

        // read the number of locations in the polyline
        final int size = input.readFlexibleShort();

        // and create an array for their long values in DM5
        final var decodedLocationsInDm5 = new long[size];

        // then read the initial latitude
        final var startLatitude = input.readInt();

        // and put it in the array
        decodedLocationsInDm5[0] = Location.toLong(startLatitude, 0);

        // before we decode each latitude offset
        index = 1;
        lastInDm5 = startLatitude;
        codec.decode(input, (ordinal, offset) ->
        {
            // and if the offset is zero, we've reached a destination latitude
            if (offset == 0)
            {
                // so store just the latitude in the location array
                decodedLocationsInDm5[index++] = Location.toLong(lastInDm5, 0);

                // and continue decoding points if we aren't at the last point already
                return index == size ? STOP : CONTINUE;
            }

            // otherwise, just add the offset to the last latitude and continue
            lastInDm5 += offset;
            return CONTINUE;
        });

        // Next, we read the start longitude
        final var startLongitude = input.readInt();

        // and put it in the array along with the start latitude
        decodedLocationsInDm5[0] = Location.toLong(startLatitude, startLongitude);

        // before we decode each longitude offset
        index = 1;
        lastInDm5 = startLongitude;
        codec.decode(input, (ordinal, offset) ->
        {
            // and if the offset is zero, we've reached a destination longitude
            if (offset == END_OF_OFFSETS)
            {
                // so store the longitude, along with the latitude we found above, in the location array
                final var lastLatitude = Location.latitude(decodedLocationsInDm5[index - 1]);
                decodedLocationsInDm5[index++] = Location.toLong(lastLatitude, lastInDm5 + offset);

                // and continue decoding points if we aren't at the last point already
                return index == size ? STOP : CONTINUE;
            }

            // otherwise, just add the offset to the last latitude and continue
            lastInDm5 += offset;
            return CONTINUE;
        });

        return decodedLocationsInDm5;
    }

    public void encode(final ByteArray output, final Polyline polyline)
    {
        // We only support polylines with fewer than 64K locations
        output.writeFlexibleShort((short) polyline.size());
        writeLatitudes(output, polyline);
        writeLongitudes(output, polyline);
    }

    private void initialize()
    {
        final var symbols = codec.codedSymbols();
        offsetsInDm5 = new int[symbols.size() / 2];
        var i = 0;
        for (final var symbol : symbols)
        {
            if (symbol.value() > 0)
            {
                offsetsInDm5[i++] = symbol.value();
            }
        }
        Arrays.sort(offsetsInDm5);
    }

    private int offsetInDm5(int destination)
    {
        // While we have not arrived at the destination offset,
        final var sign = destination < 0 ? -1 : 1;
        destination = Math.abs(destination);

        // search for the largest offset that we can apply
        final var size = offsetsInDm5.length;
        for (var index = 0; index < size; index++)
        {
            var offset = offsetsInDm5[index];
            final var step = destination - offset;
            if (step == 0)
            {
                return sign * offset;
            }
            else if (step < 0)
            {
                offset = offsetsInDm5[index - 1];
                return sign * offset;
            }
        }
        return offsetsInDm5[size - 1];
    }

    private void writeLatitudes(final ByteArray output, final Polyline polyline)
    {
        output.writeInt(polyline.get(0).latitude().asDm5());
        index = 1;
        destination = 0;
        codec.encode(output, ordinal ->
        {
            if (destination == 0)
            {
                if (index == polyline.size())
                {
                    return null;
                }
                final var current = polyline.get(index);
                final var previous = polyline.get(index - 1);
                destination = current.latitude().asDm5() - previous.latitude().asDm5();
                index++;
                if (index > 2)
                {
                    return END_OF_OFFSETS;
                }
            }

            final var offset = offsetInDm5(destination);
            destination -= offset;
            return offset;
        });
    }

    private void writeLongitudes(final ByteArray output, final Polyline polyline)
    {
        output.writeInt(polyline.get(0).longitude().asDm5());
        index = 1;
        destination = 0;
        codec.encode(output, ignored ->
        {
            if (destination == 0)
            {
                if (index == polyline.size())
                {
                    output.write((byte) 0);
                    return null;
                }
                final var current = polyline.get(index);
                final var previous = polyline.get(index - 1);
                destination = current.longitude().asDm5() - previous.longitude().asDm5();
                index++;
                if (index > 2)
                {
                    return END_OF_OFFSETS;
                }
            }

            final var offset = offsetInDm5(destination);
            destination -= offset;
            return offset;
        });
    }
}
