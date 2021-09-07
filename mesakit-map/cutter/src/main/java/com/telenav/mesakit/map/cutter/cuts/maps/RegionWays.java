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

package com.telenav.mesakit.map.cutter.cuts.maps;

import com.telenav.kivakit.collections.map.MultiMap;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine.KivaKitExcludeFromSizeOf;
import com.telenav.kivakit.primitive.collections.map.split.SplitLongToIntMap;
import com.telenav.mesakit.map.data.formats.pbf.model.entities.PbfWay;
import com.telenav.mesakit.map.region.Region;
import com.telenav.mesakit.map.region.RegionSet;
import com.telenav.mesakit.map.region.project.RegionLimits;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegionWays
{
    /** Map between region indexes and regions */
    @KivaKitExcludeFromSizeOf
    private final RegionIndexMap regionIndexMap;

    /** Set of regions that are in this store */
    @KivaKitExcludeFromSizeOf
    private final Set<Integer> regionIndexes = new HashSet<>();

    /** Primary (first) region for each way */
    private final SplitLongToIntMap regionForWay;

    /**
     * Any additional regions for each way (this is not very space efficient, but there are also not very many ways that
     * span regions like this)
     */
    private final MultiMap<Long, Integer> moreRegionsForWay = new MultiMap<>();

    public RegionWays(final String name, final RegionIndexMap regionIndexMap)
    {
        this.regionIndexMap = regionIndexMap;

        regionForWay = new SplitLongToIntMap(name + ".regionForWay");
        regionForWay.initialSize(RegionLimits.ESTIMATED_WAYS);
        regionForWay.initialize();
    }

    public void add(final int regionIndex, final long wayIdentifier)
    {
        final var index = regionForWay.get(wayIdentifier);
        if (regionForWay.isNull(index))
        {
            regionForWay.put(wayIdentifier, regionIndex);
        }
        else if (regionIndex != index)
        {
            moreRegionsForWay.add(wayIdentifier, regionIndex);
        }
    }

    public void add(final Region<?> region, final PbfWay way)
    {
        add(regionIndexMap.indexForRegion(region), way.identifierAsLong());
    }

    public void addAll(final int regionIndex, final List<Way> ways)
    {
        // Add the region index to our set
        regionIndexes.add(regionIndex);

        // Go through each way
        for (final var way : ways)
        {
            // and add it to the given region
            add(regionIndex, way.getId());
        }
    }

    public boolean contains(final int regionIndex, final long wayIdentifier)
    {
        for (final var index : regionIndexes(wayIdentifier))
        {
            if (index == regionIndex)
            {
                return true;
            }
        }
        return false;
    }

    public RegionSet regions()
    {
        return regionIndexMap.regionsForIndexes(regionIndexes);
    }

    public RegionSet regions(final long wayIdentifier)
    {
        return regionIndexMap.regionsForIndexes(regionIndexes(wayIdentifier));
    }

    private List<Integer> regionIndexes(final long wayIdentifier)
    {
        final List<Integer> indexes = new ArrayList<>();
        final var regionIndex = regionForWay.get(wayIdentifier);
        if (!regionForWay.isNull(regionIndex))
        {
            indexes.add(regionIndex);
            final List<Integer> more = moreRegionsForWay.get(wayIdentifier);
            if (more != null)
            {
                indexes.addAll(more);
            }
        }
        return indexes;
    }
}
