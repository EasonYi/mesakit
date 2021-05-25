package com.telenav.mesakit.map.data.formats.pbf.model.tags;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.iteration.Iterators;
import com.telenav.kivakit.kernel.language.primitives.Ints;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.mesakit.map.data.formats.pbf.project.lexakai.diagrams.DiagramPbfModelTags;
import org.jetbrains.annotations.NotNull;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.Iterator;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPbfModelTags.class)
public class EmptyPbfTagMap extends PbfTagMap
{
    protected EmptyPbfTagMap()
    {
        super(0);
    }

    @Override
    public boolean containsKey(final String key)
    {
        return false;
    }

    @Override
    public boolean equals(final Object object)
    {
        return super.equals(object);
    }

    @Override
    public String get(final String key)
    {
        return null;
    }

    @Override
    public String get(final String key, final String defaultValue)
    {
        return null;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public @NotNull Iterator<Tag> iterator()
    {
        return Iterators.empty();
    }

    @Override
    public Iterator<String> keys()
    {
        return Iterators.empty();
    }

    @Override
    public void put(final String key, final String value)
    {
        unsupported();
    }

    @Override
    public void putAll(final Iterable<Tag> tags)
    {
        unsupported();
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public Tag tag(final String key)
    {
        return null;
    }

    @Override
    public String value(final String key)
    {
        return null;
    }

    @Override
    public int valueAsInt(final String key)
    {
        return Ints.INVALID;
    }

    @Override
    public int valueAsNaturalNumber(final String key)
    {
        return Ints.INVALID;
    }

    @Override
    public StringList valueSplit(final String key)
    {
        return new StringList();
    }
}
