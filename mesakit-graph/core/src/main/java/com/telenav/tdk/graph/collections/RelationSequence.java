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

package com.telenav.tdk.graph.collections;

import com.telenav.tdk.core.kernel.interfaces.object.Matcher;
import com.telenav.tdk.core.kernel.language.iteration.Streams;
import com.telenav.tdk.core.kernel.language.matching.Matching;
import com.telenav.tdk.core.kernel.language.string.StringList;
import com.telenav.tdk.core.kernel.scalars.counts.Count;
import com.telenav.tdk.core.kernel.scalars.counts.Maximum;
import com.telenav.tdk.graph.EdgeRelation;
import com.telenav.tdk.graph.Route;
import com.telenav.tdk.graph.io.load.GraphConstraints;
import com.telenav.tdk.graph.project.TdkGraphCoreLimits.Estimated;
import com.telenav.tdk.graph.project.TdkGraphCoreLimits.Limit;
import com.telenav.tdk.map.geography.rectangle.Bounded;
import com.telenav.tdk.map.geography.rectangle.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * An arbitrary sequence of relations. Wraps {@link Iterable} and adds convenience methods for working with relation
 * sequences.
 *
 * @author jonathanl (shibo)
 */
public class RelationSequence implements Iterable<EdgeRelation>, Bounded
{
    /**
     * An empty relation sequence
     */
    public static RelationSequence EMPTY = new RelationSequence(RelationSet.EMPTY);

    /** The relations in this sequence */
    private final Iterable<EdgeRelation> relations;

    /** The bounding rectangle of this sequence */
    private Rectangle bounds;

    /**
     * Construct relation sequence
     */
    public RelationSequence(final Iterable<EdgeRelation> relations)
    {
        this.relations = relations;
    }

    public EdgeSet asEdgeSet()
    {
        final var edges = new EdgeSet(Limit.EDGES_PER_RELATION, Estimated.EDGES_PER_RELATION);
        for (final var relation : this)
        {
            edges.addAll(relation.edgeSet());
        }
        return edges;
    }

    /**
     * @return This relation sequence as a list
     */
    public List<EdgeRelation> asList()
    {
        final List<EdgeRelation> list = new ArrayList<>();
        for (final var relation : this)
        {
            list.add(relation);
        }
        return list;
    }

    /**
     * @return This relation sequence as a route. If the relation sequence doesn't form a valid route, an exception will
     * be thrown.
     */
    public Route asRoute()
    {
        return Route.forEdges(asEdgeSet());
    }

    /**
     * @return This sequence as an {@link RelationSet}
     */
    public RelationSet asSet(final Maximum maximum)
    {
        return RelationSet.forIterable(maximum, this);
    }

    /**
     * @return The bounding rectangle for this sequence of relations
     */
    @Override
    public Rectangle bounds()
    {
        if (bounds == null)
        {
            bounds = Rectangle.fromBoundedObjects(relations);
        }
        return bounds;
    }

    /**
     * @return The number of relations in this sequence
     */
    public Count count()
    {
        return Count.of(this);
    }

    /**
     * @param maximum A count after which the program will stop iterating the iterable and return stopAfter.
     * @return The count is smaller than stopAfter, or stopAfter otherwise.
     */
    public Count count(final Count maximum)
    {
        return Count.of(this, maximum);
    }

    /**
     * @return The relations in this sequence within the given bounds
     */
    public RelationSequence intersecting(final Rectangle bounds)
    {
        return matching(relation -> bounds.intersects(relation.bounds()));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<EdgeRelation> iterator()
    {
        return relations.iterator();
    }

    /**
     * @return The sequence of relations matching the given graph constraints
     */
    public RelationSequence matching(final GraphConstraints constraints)
    {
        return constraints.relations(relations);
    }

    /**
     * @return The relations in this sequence that match the given matcher
     */
    public RelationSequence matching(final Matcher<EdgeRelation> matcher)
    {
        return new RelationSequence(new Matching<>(matcher)
        {
            @Override
            protected Iterator<EdgeRelation> values()
            {
                return relations.iterator();
            }
        });
    }

    public Stream<EdgeRelation> parallelStream()
    {
        return Streams.parallelStream(this);
    }

    public Stream<EdgeRelation> stream()
    {
        return Streams.stream(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final var relations = new StringList();
        for (final var relation : this.relations)
        {
            relations.add(relation.identifier().toString());
        }
        return "[EdgeRelationSequence relations = " + relations.join(";") + ", bounds = " + bounds + "]";
    }

    /**
     * @return The relations in this sequence within the given bounds
     */
    public RelationSequence within(final Rectangle bounds)
    {
        return matching(relation -> bounds.intersects(relation.bounds()));
    }
}
