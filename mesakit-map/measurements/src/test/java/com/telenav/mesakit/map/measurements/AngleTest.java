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

package com.telenav.mesakit.map.measurements;

import com.telenav.mesakit.map.measurements.geographic.Angle;
import com.telenav.mesakit.map.measurements.geographic.Angle.Chirality;
import com.telenav.mesakit.map.measurements.project.MapMeasurementsUnitTest;
import org.junit.Test;

public class AngleTest extends MapMeasurementsUnitTest
{
    @Test
    public void testAdd()
    {
        final var angle1 = random().newAngle();
        final var angle2 = random().newAngle();

        // Retrieve the nanodegrees and then make sure that it doesn't wrap around 360.
        final var totalNanodegrees = angle1.asNanodegrees() + angle2.asNanodegrees();
        final var normalizedNanodegrees = totalNanodegrees % 360_000_000_000L;

        ensureEqual(normalizedNanodegrees, angle1.plus(angle2).asNanodegrees());
    }

    @Test
    public void testBetween()
    {
        // Ensure the case where everything is positive.
        var minimum = random().newAngle(Angle.degrees(45), Angle.degrees(90));
        var maximum = random().newAngle(Angle.degrees(180), Angle.degrees(225));
        var isBetween = random().newAngle(minimum, maximum);
        var notBetween = random().newAngle(Angle.degrees(0), Angle.degrees(45));

        ensure(isBetween.isBetween(minimum, maximum, Chirality.CLOCKWISE));
        ensureFalse(notBetween.isBetween(minimum, maximum, Chirality.CLOCKWISE));

        // Ensure the contract conditions where the angle falls on the minimum and maximums.
        ensure(minimum.isBetween(minimum, maximum, Chirality.CLOCKWISE));
        ensure(maximum.isBetween(minimum, maximum, Chirality.CLOCKWISE));

        // Now check the conditions where minimum and maximum span 0.
        minimum = random().newAngle(Angle.degrees(-90), Angle.degrees(-45));
        maximum = random().newAngle(Angle.degrees(45), Angle.degrees(90));
        isBetween = random().newAngle(minimum, maximum);
        notBetween = random().newAngle(Angle.degrees(-180), Angle.degrees(-90));

        ensure(isBetween.isBetween(minimum, maximum, Chirality.CLOCKWISE));
        ensureFalse(notBetween.isBetween(minimum, maximum, Chirality.CLOCKWISE));

        ensure(Angle.degrees(45).isBetween(Angle.degrees(10), Angle.degrees(50), Chirality.CLOCKWISE));
        ensure(Angle.degrees(45).isBetween(Angle.degrees(55), Angle.degrees(50), Chirality.CLOCKWISE));
        ensureFalse(Angle.degrees(45).isBetween(Angle.degrees(50), Angle.degrees(55), Chirality.CLOCKWISE));

        ensure(Angle.degrees(45).isBetween(Angle.degrees(50), Angle.degrees(10), Chirality.COUNTERCLOCKWISE));
        ensure(Angle.degrees(45).isBetween(Angle.degrees(50), Angle.degrees(55), Chirality.COUNTERCLOCKWISE));
        ensureFalse(Angle.degrees(45).isBetween(Angle.degrees(55), Angle.degrees(50), Chirality.COUNTERCLOCKWISE));
    }

    @Test
    public void testDifference()
    {
        final var angle1 = random().newAngle();
        final var angle2 = random().newAngle();

        final var difference = Math.abs(angle1.asNanodegrees() - angle2.asNanodegrees());

        ensureEqual(difference, angle1.absoluteDifference(angle2).asNanodegrees());
        ensureEqual(Angle.degrees(10), Angle.degrees(10).difference(Angle.degrees(20), Chirality.CLOCKWISE));
        ensureEqual(Angle.degrees(10), Angle.degrees(355).difference(Angle.degrees(5), Chirality.CLOCKWISE));
        ensureEqual(Angle.degrees(10), Angle.degrees(20).difference(Angle.degrees(10), Chirality.COUNTERCLOCKWISE));
        ensureEqual(Angle.degrees(10), Angle.degrees(5).difference(Angle.degrees(355), Chirality.COUNTERCLOCKWISE));
        ensureEqual(Angle.degrees(10), Angle.degrees(5).difference(Angle.degrees(15), Chirality.SMALLEST));
        ensureEqual(Angle.degrees(10), Angle.degrees(355).difference(Angle.degrees(5), Chirality.SMALLEST));
    }

    @Test
    public void testGreaterThan()
    {
        final var smallAngle = random().newAngle(Angle.degrees(-180), Angle.degrees(180));
        final var bigAngle = random().newAngle(Angle.degrees(180), Angle.degrees(360));

        ensure(bigAngle.isGreaterThan(smallAngle));
        ensureFalse(smallAngle.isGreaterThan(bigAngle));
        ensureFalse(bigAngle.isGreaterThan(bigAngle));
    }

    @Test
    public void testGreaterThanOrEqualTo()
    {
        final var smallAngle = random().newAngle(Angle.degrees(-180), Angle.degrees(180));
        final var bigAngle = random().newAngle(Angle.degrees(180), Angle.degrees(360));

        ensure(bigAngle.isGreaterThanOrEqualTo(smallAngle));
        ensureFalse(smallAngle.isGreaterThanOrEqualTo(bigAngle));
        ensure(bigAngle.isGreaterThanOrEqualTo(bigAngle));
    }

    @Test
    public void testIsClose()
    {
        ensure(Angle.degrees(45).isClose(Angle.degrees(50), Angle.degrees(15)));
        ensureFalse(Angle.degrees(45).isClose(Angle.degrees(50), Angle.degrees(1)));
        ensure(Angle.degrees(355).isClose(Angle.degrees(5), Angle.degrees(15)));
        ensureFalse(Angle.degrees(355).isClose(Angle.degrees(5), Angle.degrees(1)));
    }

    @Test
    public void testLessThan()
    {
        final var smallAngle = random().newAngle(Angle.degrees(-180), Angle.degrees(180));
        final var bigAngle = random().newAngle(Angle.degrees(180), Angle.degrees(360));

        ensure(smallAngle.isLessThan(bigAngle));
        ensureFalse(bigAngle.isLessThan(smallAngle));
        ensureFalse(bigAngle.isLessThan(bigAngle));
    }

    @Test
    public void testLessThanOrEqualTo()
    {
        final var smallAngle = random().newAngle(Angle.degrees(-180), Angle.degrees(180));
        final var bigAngle = random().newAngle(Angle.degrees(180), Angle.degrees(360));

        ensure(smallAngle.isLessThanOrEqualTo(bigAngle));
        ensureFalse(bigAngle.isLessThanOrEqualTo(smallAngle));
        ensure(bigAngle.isLessThanOrEqualTo(bigAngle));
    }

    @Test
    public void testMaximum()
    {
        final var smallAngle = random().newAngle(Angle.degrees(-180), Angle.degrees(180));
        final var bigAngle = random().newAngle(Angle.degrees(180), Angle.degrees(360));

        ensureEqual(bigAngle, smallAngle.maximum(bigAngle));
        ensureEqual(bigAngle, bigAngle.maximum(smallAngle));
        ensureEqual(bigAngle, bigAngle.maximum(bigAngle));
    }

    @Test
    public void testMinimum()
    {
        final var smallAngle = random().newAngle(Angle.degrees(-180), Angle.degrees(180));
        final var bigAngle = random().newAngle(Angle.degrees(180), Angle.degrees(360));

        ensureEqual(smallAngle, smallAngle.minimum(bigAngle));
        ensureEqual(smallAngle, bigAngle.minimum(smallAngle));
        ensureEqual(smallAngle, smallAngle.minimum(smallAngle));
    }

    @Test
    public void testScaleBy()
    {
        final var angle = random().newAngle();
        final var multiplier = random().newDouble(0, 10000);

        // Test the standard case.
        final var expected = (angle.asNanodegrees() * multiplier) % 360_000_000_000L;
        ensureEqual(angle.times(multiplier).asNanodegrees(), (long) expected);
    }

    @Test
    public void testSubtract()
    {
        final var angle1 = random().newAngle();
        final var angle2 = random().newAngle();

        // Retrieve the nanodegrees and then make sure that it doesn't wrap around 360.
        final var nanodegreesDifference = angle1.asNanodegrees() - angle2.asNanodegrees();
        final var normalizedNanodegrees = nanodegreesDifference % 360_000_000_000L;

        ensureEqual(normalizedNanodegrees, angle1.minus(angle2).asNanodegrees());

        // Verify validity of negative angle
        final var angle3 = Angle.degrees(10.0).minus(Angle.degrees(20.0));
        ensureWithin(-10.0, angle3.asDegrees(), 0.0);
    }
}
