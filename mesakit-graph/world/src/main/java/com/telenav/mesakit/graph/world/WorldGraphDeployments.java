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

import com.telenav.kivakit.configuration.Deployment;
import com.telenav.kivakit.configuration.DeploymentSet;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;

/**
 * {@link WorldGraphDeployments} is a {@link DeploymentSet} that includes deployment configurations for a few built-in
 * world graph deployments (local, osmteam and navteam).
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see DeploymentSet
 */
public class WorldGraphDeployments extends DeploymentSet
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A deployment of the world graph with a local repository at ~/tdk/graph/world-graph/repositories/local
     */
    public static Deployment localDeployment()
    {
        return LOGGER.listenTo(new Deployment("local", "developer laptop"))
                .addPackage(WorldGraph.class, "configuration/local");
    }

    /**
     * A set of 4 built-in deployments loaded from packages: local, osmteam and navteam. In addition, any deployments
     * found in the folder ~/tdk/graph/world-graph/deployments are included.
     */
    public WorldGraphDeployments(final Listener listener)
    {
        listener.listenTo(this);
        addDeployments(tdkWorldGraphDeploymentsFolder());
        add(localDeployment());
    }

    private Folder tdkWorldGraphDeploymentsFolder()
    {
        return Folder.kivakitCache().folder("world-graph/deployments");
    }
}
