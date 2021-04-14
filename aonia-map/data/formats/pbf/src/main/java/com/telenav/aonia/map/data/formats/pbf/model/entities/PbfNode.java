package com.telenav.aonia.map.data.formats.pbf.model.entities;

import com.telenav.aonia.map.data.formats.pbf.model.identifiers.PbfNodeIdentifier;
import com.telenav.aonia.map.data.formats.pbf.project.lexakai.diagrams.DiagramPbfModelEntities;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPbfModelEntities.class)
public class PbfNode extends PbfEntity<Node>
{
    public PbfNode(final Node node)
    {
        super(node);
    }

    @Override
    public PbfNodeIdentifier identifier()
    {
        return new PbfNodeIdentifier(identifierAsLong());
    }

    public double latitude()
    {
        return get().getLatitude();
    }

    public double longitude()
    {
        return get().getLongitude();
    }
}
