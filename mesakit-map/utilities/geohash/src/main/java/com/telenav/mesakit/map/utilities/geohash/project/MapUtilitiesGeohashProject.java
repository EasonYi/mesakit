package com.telenav.mesakit.map.utilities.geohash.project;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class MapUtilitiesGeohashProject extends Project
{
    private static final Lazy<MapUtilitiesGeohashProject> project = Lazy.of(MapUtilitiesGeohashProject::new);

    public static MapUtilitiesGeohashProject get()
    {
        return project.get();
    }

    protected MapUtilitiesGeohashProject()
    {
    }
}
