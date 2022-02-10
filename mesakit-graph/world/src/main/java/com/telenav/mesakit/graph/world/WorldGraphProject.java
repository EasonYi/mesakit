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

package com.telenav.mesakit.graph.world;

import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.serialization.core.BinarySerializationSessionFactory;
import com.telenav.mesakit.graph.GraphProject;
import com.telenav.mesakit.graph.world.project.WorldGraphKryoTypes;

public class WorldGraphProject extends Project
{
    private static final Lazy<WorldGraphProject> singleton = Lazy.of(WorldGraphProject::new);

    public static WorldGraphProject get()
    {
        return singleton.get();
    }

    protected WorldGraphProject()
    {
        BinarySerializationSessionFactory.threadLocal(new WorldGraphKryoTypes().sessionFactory());
    }

    @Override
    public ObjectSet<Project> dependencies()
    {
        return ObjectSet.objectSet(GraphProject.get());
    }

    /**
     * NOTE: This version may sometimes be older or newer than the framework version
     *
     * @return The current world graph version.
     */
    public Version worldGraphVersion()
    {
        return KivaKit.get().kivakitVersion();
    }
}
