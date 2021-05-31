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

package com.telenav.tdk.navigation.routing.dijkstra;

import com.telenav.mesakit.graph.Edge;
import com.telenav.mesakit.graph.Route;

public enum Direction
{
    FORWARD,
    BACKWARD;

    public Route concatenate(final Route route, final Edge candidate)
    {
        return isForward() ? route.append(candidate) : route.prepend(candidate);
    }

    public boolean isForward()
    {
        return this == FORWARD;
    }

    public Direction reversed()
    {
        return isForward() ? BACKWARD : FORWARD;
    }
}
