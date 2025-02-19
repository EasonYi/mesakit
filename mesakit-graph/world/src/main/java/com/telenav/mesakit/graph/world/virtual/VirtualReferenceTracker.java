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

package com.telenav.mesakit.graph.world.virtual;

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.LinkedList;

public class VirtualReferenceTracker<T> extends BaseRepeater
{
    /** True to turn on GC debugging */
    private static final boolean DEBUG_GARBAGE_COLLECTION = false;

    /** List of loaded references */
    @UmlAggregation(label = "loads, weakens, hardens")
    private final LinkedList<VirtualReference<T>> loaded = new LinkedList<>();

    /** The approximate maximum amount of memory to hard reference */
    private final Bytes maximum;

    /** Reference queue for notifications that soft and weak references have been collected */
    private final ReferenceQueue<T> queue = new ReferenceQueue<>();

    /** The current memory consumption estimate */
    private Bytes total = Bytes._0;

    /** CheckType of references to keep */
    @UmlAggregation
    private final VirtualReferenceType type;

    public VirtualReferenceTracker(Bytes maximum, VirtualReferenceType type)
    {
        this.maximum = Ensure.ensureNotNull(maximum);
        this.type = Ensure.ensureNotNull(type);

        trace("Reference tracker keeping hard references to approximately $", maximum);

        // NOTE: This debug feature will cause hard references to stay live even if nothing
        // references this tracker anymore
        if (DEBUG_GARBAGE_COLLECTION)
        {
            // Show any references that get garbage collected
            var thread = new Thread(() ->
            {
                while (true)
                {
                    try
                    {
                        var unreferenced = queue.remove();
                        trace("Garbage collected $", ((Named) unreferenced).name());
                    }
                    catch (InterruptedException ignored)
                    {
                    }
                }
            });
            thread.setName("ReferenceTracker-Garbage-Collection-Monitor");
            thread.setDaemon(true);
            thread.start();
        }
    }

    synchronized void onLoaded(VirtualReference<T> reference)
    {
        assert reference != null : "Reference must not be null";

        // Increase memory consumption by the size of the referenced value
        total = total.plus(reference.memorySize());

        // Add the given reference to the end of the list of hardened references
        loaded.addLast(reference);

        // If we are using too much memory,
        while (total.isGreaterThan(maximum))
        {
            // show debug details (using a copy of the loaded list since we will be modifying it)
            trace("Total of $ exceeds maximum of $: $", total, maximum, new ArrayList<>(loaded));

            // soften the reference that we loaded the longest ago
            var oldest = loaded.removeFirst();
            trace("Softening $", oldest.name());
            oldest.soften();

            // and reduce hard-referenced memory estimate
            total = total.minus(oldest.memorySize());
        }

        // Give details of how much is currently being hard-referenced
        trace("Hard referencing $ of $", total, maximum);
    }

    ReferenceQueue<T> queue()
    {
        return queue;
    }

    VirtualReferenceType type()
    {
        return type;
    }
}