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

package com.telenav.aonia.map.road.model;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

public class RoadLimits
{
    public static final Maximum WORDS_PER_ROAD_NAME = Maximum.parse("50");

    public static final Maximum ROAD_NAMES = Maximum.parse("20,000,000");

    public static final Maximum SHIELD_ICONS = Maximum.parse("20,000");

    public static final Maximum EXIT_NUMBERS = Maximum.parse("20,000");
}
