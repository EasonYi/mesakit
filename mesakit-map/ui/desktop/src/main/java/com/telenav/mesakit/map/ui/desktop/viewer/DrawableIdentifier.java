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

package com.telenav.mesakit.map.ui.desktop.viewer;

import com.telenav.mesakit.map.ui.desktop.graphics.drawables.MapDrawable;

/**
 * Any unique identifier for a {@link MapDrawable} object
 *
 * @author jonathanl (shibo)
 */
public class DrawableIdentifier
{
    private final Object object;

    public DrawableIdentifier(Object object)
    {
        this.object = object;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof DrawableIdentifier)
        {
            var that = (DrawableIdentifier) object;
            return this.object.equals(that.object);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return object.hashCode();
    }

    @Override
    public String toString()
    {
        return object.toString();
    }
}
