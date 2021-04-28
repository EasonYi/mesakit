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

package com.telenav.mesakit.map.geography.indexing.rtree;

import com.telenav.mesakit.map.geography.shape.rectangle.Bounded;
import com.telenav.mesakit.map.geography.shape.rectangle.Intersectable;
import com.telenav.mesakit.map.measurements.geographic.Angle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Takes a list of bounded objects and splits the list into two nodes, returning the parent node. The split is either
 * done horizontally or vertically, depending on which direction has more span.
 */
abstract class LinearSplit<T extends Bounded & Intersectable>
{
    protected abstract InteriorNode<T> onSplit(T a, T b);

    protected InteriorNode<T> split(final Iterable<T> objects)
    {
        final List<T> latitudeSorted = new ArrayList<>();
        final List<T> longitudeSorted = new ArrayList<>();
        objects.forEach(latitudeSorted::add);
        objects.forEach(longitudeSorted::add);
        latitudeSorted.sort(Comparator.comparing(t -> t.bounds().center().latitude()));
        longitudeSorted.sort(Comparator.comparing(t -> t.bounds().center().longitude()));
        final var latitudeMinimum = latitudeSorted.get(0);
        final var latitudeMaximum = latitudeSorted.get(latitudeSorted.size() - 1);
        final var longitudeMinimum = longitudeSorted.get(0);
        final var longitudeMaximum = longitudeSorted.get(longitudeSorted.size() - 1);
        final Angle latitudeDifference = latitudeMaximum.bounds().center().latitude()
                .minus(latitudeMinimum.bounds().center().latitude());
        final Angle longitudeDifference = longitudeMaximum.bounds().center().longitude()
                .minus(longitudeMinimum.bounds().center().longitude());
        if (latitudeDifference.isGreaterThan(longitudeDifference))
        {
            return onSplit(latitudeMinimum, latitudeMaximum);
        }
        else
        {
            return onSplit(longitudeMinimum, longitudeMaximum);
        }
    }
}
