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

package com.telenav.mesakit.map.region;

import com.telenav.mesakit.map.region.project.lexakai.diagrams.DiagramRegion;
import com.telenav.kivakit.core.kernel.language.values.identifier.IntegerIdentifier;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * A unique identifier for a region which cannot be persisted or relied on in the long-term.
 */
@UmlClassDiagram(diagram = DiagramRegion.class)
@UmlExcludeSuperTypes
public class RegionIdentifier extends IntegerIdentifier
{
    public RegionIdentifier(final int identifier)
    {
        super(identifier);
    }

    public RegionIdentifier next()
    {
        return new RegionIdentifier(asInteger() + 1);
    }
}
