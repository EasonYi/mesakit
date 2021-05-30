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

package com.telenav.tdk.graph.traffic.project;

import com.telenav.tdk.core.kernel.language.collections.set.Sets;
import com.telenav.tdk.core.kernel.language.io.serialization.TdkSerializer;
import com.telenav.tdk.core.kernel.language.object.Lazy;
import com.telenav.tdk.core.kernel.project.TdkProject;
import com.telenav.tdk.map.geography.project.TdkMapGeography;

import java.util.Set;

public class TdkGraphTraffic extends TdkProject
{
    private static final Lazy<TdkGraphTraffic> singleton = new Lazy<>(TdkGraphTraffic::new);

    public static TdkGraphTraffic get()
    {
        return singleton.get();
    }

    protected TdkGraphTraffic()
    {
    }

    @Override
    public Set<TdkProject> dependencies()
    {
        return Sets.of(TdkMapGeography.get());
    }

    @Override
    public TdkSerializer newSerializer()
    {
        return new TdkGraphTrafficKryoSerializer();
    }
}
