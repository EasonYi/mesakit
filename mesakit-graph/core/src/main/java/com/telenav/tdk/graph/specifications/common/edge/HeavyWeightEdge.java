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


package com.telenav.tdk.graph.specifications.common.edge;

import com.telenav.tdk.core.kernel.language.collections.list.ObjectList;
import com.telenav.tdk.core.kernel.language.object.Maybe;
import com.telenav.tdk.core.kernel.language.reflection.property.filters.TdkExcludeProperty;
import com.telenav.tdk.core.kernel.scalars.counts.*;
import com.telenav.tdk.core.kernel.time.*;
import com.telenav.tdk.data.formats.library.map.identifiers.NodeIdentifier;
import com.telenav.tdk.data.formats.pbf.model.change.*;
import com.telenav.tdk.data.formats.pbf.model.identifiers.PbfNodeIdentifier;
import com.telenav.tdk.data.formats.pbf.model.tags.PbfTagList;
import com.telenav.tdk.graph.*;
import com.telenav.tdk.graph.identifiers.*;
import com.telenav.tdk.graph.metadata.DataSpecification;
import com.telenav.tdk.graph.specifications.osm.graph.edge.model.attributes.OsmEdgeAttributes;
import com.telenav.tdk.graph.traffic.historical.SpeedPatternIdentifier;
import com.telenav.tdk.graph.traffic.roadsection.RoadSectionIdentifier;
import com.telenav.tdk.map.geography.Location;
import com.telenav.tdk.map.geography.polyline.Polyline;
import com.telenav.tdk.map.geography.rectangle.Rectangle;
import com.telenav.tdk.map.measurements.*;
import com.telenav.tdk.map.region.*;
import com.telenav.tdk.map.road.model.*;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.*;

import static com.telenav.tdk.core.kernel.validation.Validate.ensure;
import static com.telenav.tdk.map.road.model.RoadState.*;

public class HeavyWeightEdge extends Edge
{
    private int index;

    private Boolean fromVertexClipped;

    private Boolean isClosedToThroughTraffic;

    private Boolean isDoubleDigitized;

    private Boolean isTollRoad;

    private Boolean isUnderConstruction;

    private Boolean toVertexClipped;

    private BridgeType bridgeType;

    private Count hovLaneCount;

    private Count laneCount;

    private Count traceCount;

    private Country country;

    private County county;

    private Location fromLocation;

    private Distance length;

    private Type type;

    private EdgeIdentifier rawIdentifier;

    private MetropolitanArea metropolitanArea;

    private NodeIdentifier fromNodeIdentifier;

    private NodeIdentifier toNodeIdentifier;

    private Polyline roadShape;

    private Rectangle bounds;

    private RoadFunctionalClass roadFunctionalClass;

    private RoadState roadState;

    private RoadSubType roadSubType;

    private RoadSurface surface;

    private RoadType roadType;

    private Speed referenceSpeed;

    private Speed reverseReferenceSpeed;

    private final Map<RoadName.Type, List<RoadName>> roadNames = new HashMap<>();

    private Speed speedLimit;

    private SpeedCategory freeFlow;

    private SpeedPatternIdentifier reverseSpeedPatternIdentifier;

    private SpeedPatternIdentifier speedPatternIdentifier;

    private State state;

    private Vertex from;

    private Vertex to;

    private Location toLocation;

    private PbfChangeSetIdentifier pbfChangeSetIdentifier;

    private PbfRevisionNumber pbfRevisionNumber;

    private PbfUserIdentifier pbfUserIdentifier;

    private PbfUserName pbfUserName;

    private Time lastModificationTime;

    private PbfTagList tags = PbfTagList.EMPTY;

    private Iterable<RoadSectionIdentifier> tmcIdentifiers;

    private Iterable<RoadSectionIdentifier> reverseTmcIdentifiers;

    private GradeSeparation fromGradeSeparation;

    private GradeSeparation toGradeSeparation;

    /**
     * It is not permissible to directly construct {@link GraphElement} objects. Elements may only be constructed by a
     * {@link DataSpecification}, which ensures proper initialization and specialization of elements.
     */
    public HeavyWeightEdge(final Graph graph, final EdgeIdentifier identifier)
    {
        super(graph, identifier);
    }

    /**
     * It is not permissible to directly construct {@link GraphElement} objects. Elements may only be constructed by a
     * {@link DataSpecification}, which ensures proper initialization and specialization of elements.
     */
    public HeavyWeightEdge(final Graph graph, final long identifier)
    {
        super(graph, identifier);
    }

    /**
     * It is not permissible to directly construct {@link GraphElement} objects. Elements may only be constructed by a
     * {@link DataSpecification}, which ensures proper initialization and specialization of elements.
     */
    @SuppressWarnings("CopyConstructorMissesField")
    protected HeavyWeightEdge(final HeavyWeightEdge that)
    {
        super(that.graph(), that.identifier());
        copy(that);
    }

    public void addRoadName(final RoadName.Type type, final RoadName roadName)
    {
        ensure(type != null);
        ensure(roadName != null);
        final var names = roadNames.computeIfAbsent(type, k -> new ArrayList<>());
        names.add(roadName);
    }

    @Override
    public Rectangle bounds()
    {
        return bounds;
    }

    @Override
    public BridgeType bridgeType()
    {
        return bridgeType;
    }

    public void bridgeType(final BridgeType bridgeType)
    {
        this.bridgeType = bridgeType;
    }

    public void closedToThroughTraffic(final boolean isClosedToThroughTraffic)
    {
        this.isClosedToThroughTraffic = isClosedToThroughTraffic;
    }

    @MustBeInvokedByOverriders
    public void copy(final Edge that)
    {
        final var thatFrom = that.from();
        final var thatTo = that.to();

        index(that.index());
        type(that.type());
        from(thatFrom);
        to(thatTo);
        freeFlow(that.freeFlowSpeed());
        laneCount(that.laneCount());
        if (roadShape == null && that.isShaped())
        {
            roadShape(that.roadShape());
        }
        else
        {
            bounds = that.bounds();
        }
        fromLocation(that.fromLocation());
        toLocation(that.toLocation());
        length(that.length());
        roadState(that.roadState());
        roadSubType(that.roadSubType());
        roadType(that.roadType());
        roadFunctionalClass(that.roadFunctionalClass());

        fromGradeSeparation(that.fromGradeSeparation());
        toGradeSeparation(that.toGradeSeparation());

        tags(that.tagList());
        lastModificationTime(that.lastModificationTime());

        pbfChangeSetIdentifier(that.pbfChangeSetIdentifier());
        pbfRevisionNumber(that.pbfRevisionNumber());
        pbfUserIdentifier(that.pbfUserIdentifier());
        pbfUserName(that.pbfUserName());

        bridgeType(that.bridgeType());
        closedToThroughTraffic(that.isClosedToThroughTraffic());
        country(that.country());
        fromNodeIdentifier(that.fromNodeIdentifier());
        hovLaneCount(that.hovLaneCount());
        if (that.isTwoWay())
        {
            reverseSpeedPatternIdentifier(that.reversed().speedPatternIdentifier());
        }
        speedLimit(that.speedLimit());
        speedPatternIdentifier(that.speedPatternIdentifier());
        surface(that.roadSurface());
        tollRoad(that.isTollRoad());
        toNodeIdentifier(that.toNodeIdentifier());
        underConstruction(that.isUnderConstruction());

        if (that.supports(OsmEdgeAttributes.get().FORWARD_TMC_IDENTIFIERS))
        {
            tmcIdentifiers(that.tmcIdentifiers());
            if (that.isTwoWay())
            {
                reverseTmcIdentifiers(that.reversed().tmcIdentifiers());
            }
        }

        if (thatFrom != null)
        {
            fromVertexClipped(thatFrom.isClipped());
        }
        if (thatTo != null)
        {
            toVertexClipped(thatTo.isClipped());
        }
        copyRoadNames(that);
    }

    public void copyRoadNames(final Edge that)
    {
        if (that.supports(EdgeAttributes.get().ROAD_NAMES))
        {
            for (final var type : RoadName.Type.values())
            {
                final var names = that.roadNames(type);
                if (names != null)
                {
                    for (final var name : names)
                    {
                        addRoadName(type, name);
                    }
                }
            }
        }
    }

    @Override
    public Country country()
    {
        return country;
    }

    public void country(final Country country)
    {
        this.country = country;
    }

    @Override
    public County county()
    {
        return county;
    }

    public void county(final County county)
    {
        this.county = county;
    }

    public void freeFlow(final SpeedCategory freeFlow)
    {
        this.freeFlow = freeFlow;
    }

    @Override
    public SpeedCategory freeFlowSpeed()
    {
        return freeFlow;
    }

    @Override
    public Vertex from()
    {
        return from;
    }

    public void from(final Vertex from)
    {
        this.from = from;
        if (from != null)
        {
            fromLocation = from.location();
        }
    }

    public void fromGradeSeparation(final GradeSeparation separation)
    {
        // Grade separation can legitimately be null here because we don't store the omnipresent
        // grade separation "GROUND"
        fromGradeSeparation = separation == null ? GradeSeparation.GROUND : separation;
    }

    @Override
    public GradeSeparation fromGradeSeparation()
    {
        return fromGradeSeparation;
    }

    @Override
    public Location fromLocation()
    {
        return fromLocation;
    }

    public void fromLocation(final Location fromLocation)
    {
        assert fromLocation != null;
        this.fromLocation = fromLocation;
    }

    @Override
    public NodeIdentifier fromNodeIdentifier()
    {
        return fromNodeIdentifier;
    }

    public void fromNodeIdentifier(final NodeIdentifier identifier)
    {
        fromNodeIdentifier = identifier;
    }

    public void fromVertexClipped(final Boolean fromVertexClipped)
    {
        this.fromVertexClipped = fromVertexClipped;
    }

    @Override
    @TdkExcludeProperty
    public VertexIdentifier fromVertexIdentifier()
    {
        return from != null ? from.identifier() : null;
    }

    @Override
    public Count hovLaneCount()
    {
        return hovLaneCount;
    }

    public void hovLaneCount(final Count hovLaneCount)
    {
        this.hovLaneCount = hovLaneCount;
    }

    @Override
    public int index()
    {
        return index;
    }

    @Override
    public void index(final int index)
    {
        this.index = index;
    }

    @Override
    public boolean isClosedToThroughTraffic()
    {
        return Objects.requireNonNullElse(isClosedToThroughTraffic, false);
    }

    public void isClosedToThroughTraffic(final Boolean isClosedToThroughTraffic)
    {
        this.isClosedToThroughTraffic = isClosedToThroughTraffic;
    }

    public void isDoubleDigitized(final Boolean isDoubleDigitized)
    {
        this.isDoubleDigitized = isDoubleDigitized;
    }

    public Boolean isFromVertexClipped()
    {
        return fromVertexClipped;
    }

    @Override
    public boolean isHeavyWeight()
    {
        return true;
    }

    @Override
    public boolean isOneWay()
    {
        return roadState() == ONE_WAY;
    }

    @Override
    public boolean isSegment()
    {
        return roadShape == null;
    }

    public Boolean isToVertexClipped()
    {
        return Objects.requireNonNullElse(toVertexClipped, false);
    }

    @Override
    public boolean isTollRoad()
    {
        return Objects.requireNonNullElse(isTollRoad, false);
    }

    public void isTollRoad(final Boolean isTollRoad)
    {
        this.isTollRoad = isTollRoad;
    }

    @Override
    public boolean isTwoWay()
    {
        return roadState() == TWO_WAY;
    }

    @Override
    public boolean isUnderConstruction()
    {
        return Objects.requireNonNullElse(isUnderConstruction, false);
    }

    public void isUnderConstruction(final Boolean isUnderConstruction)
    {
        this.isUnderConstruction = isUnderConstruction;
    }

    @Override
    public Count laneCount()
    {
        return laneCount;
    }

    public void laneCount(final Count laneCount)
    {
        this.laneCount = laneCount;
    }

    @Override
    public Time lastModificationTime()
    {
        return lastModificationTime;
    }

    public void lastModificationTime(final Time lastModified)
    {
        lastModificationTime = lastModified;
    }

    @Override
    public Distance length()
    {
        return length;
    }

    public void length(final Distance length)
    {
        this.length = length;
    }

    @Override
    public MetropolitanArea metropolitanArea()
    {
        return metropolitanArea;
    }

    public void metropolitanArea(final MetropolitanArea metropolitanArea)
    {
        this.metropolitanArea = metropolitanArea;
    }

    @Override
    public Boolean osmIsDoubleDigitized()
    {
        return isDoubleDigitized;
    }

    @Override
    public Count osmTraceCount()
    {
        return traceCount;
    }

    @Override
    public PbfChangeSetIdentifier pbfChangeSetIdentifier()
    {
        return pbfChangeSetIdentifier;
    }

    public void pbfChangeSetIdentifier(final PbfChangeSetIdentifier PbfChangeSetIdentifier)
    {
        pbfChangeSetIdentifier = PbfChangeSetIdentifier;
    }

    @Override
    public PbfRevisionNumber pbfRevisionNumber()
    {
        return pbfRevisionNumber;
    }

    public void pbfRevisionNumber(final PbfRevisionNumber revision)
    {
        pbfRevisionNumber = revision;
    }

    @Override
    public PbfUserIdentifier pbfUserIdentifier()
    {
        return pbfUserIdentifier;
    }

    public void pbfUserIdentifier(final PbfUserIdentifier PbfUserIdentifier)
    {
        pbfUserIdentifier = PbfUserIdentifier;
    }

    @Override
    public PbfUserName pbfUserName()
    {
        return pbfUserName;
    }

    public void pbfUserName(final PbfUserName PbfUserName)
    {
        pbfUserName = PbfUserName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populateWithTestValues()
    {
        bridgeType(BridgeType.NONE);
        closedToThroughTraffic(false);
        country(Country.UNITED_STATES);
        county(Country.UNITED_STATES.CALIFORNIA.SAN_MATEO_COUNTY);
        freeFlow(SpeedCategory.forSpeed(Speed.FIFTY_MILES_PER_HOUR));
        from(graph().newHeavyWeightVertex(new VertexIdentifier(5)));
        fromVertexClipped(false);
        hovLaneCount(Count._0);
        laneCount(Count._1);
        length(Distance.TEN_METERS);
        metropolitanArea(Country.UNITED_STATES.CALIFORNIA.SAN_FRANCISCO_OAKLAND);
        pbfChangeSetIdentifier(new PbfChangeSetIdentifier(5));
        fromNodeIdentifier(new PbfNodeIdentifier(9));
        fromLocation(Location.TELENAV_HEADQUARTERS);
        lastModificationTime(Time.now());
        pbfRevisionNumber(new PbfRevisionNumber(22));
        tags(PbfTagList.of(new Tag("person", "shibo")));
        toNodeIdentifier(new PbfNodeIdentifier(19));
        pbfUserIdentifier(new PbfUserIdentifier(1357));
        pbfUserName(new PbfUserName("shibo"));
        rawIdentifier(new EdgeIdentifier(100));
        referenceSpeed(Speed.FIFTY_MILES_PER_HOUR);
        uniDbReverseReferenceSpeed(Speed.FIFTY_MILES_PER_HOUR);
        reverseSpeedPatternIdentifier(new SpeedPatternIdentifier(19));
        roadFunctionalClass(RoadFunctionalClass.FIRST_CLASS);
        roadNames(RoadName.Type.OFFICIAL, ObjectList.of(Maximum._8, RoadName.forName("Shibo Boulevard")));
        roadShapeAndLength(testPolyline(), testPolyline().start(), testPolyline().end());
        roadState(TWO_WAY);
        roadSubType(RoadSubType.MAIN_ROAD);
        roadType(RoadType.HIGHWAY);
        speedLimit(Speed.HIGHWAY_SPEED);
        speedPatternIdentifier(new SpeedPatternIdentifier(5));
        state(Country.UNITED_STATES.CALIFORNIA);
        surface(RoadSurface.PAVED);
        to(graph().newHeavyWeightVertex(new VertexIdentifier(5)));
        tollRoad(false);
        toVertexClipped(false);
        toLocation(Location.TELENAV_HEADQUARTERS.moved(Heading.EAST, Distance.meters(5)));
        traceCount(Count._8);
        type(Type.NORMAL);
        underConstruction(false);
        fromGradeSeparation(GradeSeparation.GROUND);
        toGradeSeparation(GradeSeparation.GROUND);
    }

    @Override
    public EdgeIdentifier rawIdentifier()
    {
        return rawIdentifier;
    }

    public void rawIdentifier(final EdgeIdentifier rawIdentifier)
    {
        this.rawIdentifier = rawIdentifier;
    }

    public void referenceSpeed(final Speed referenceSpeed)
    {
        this.referenceSpeed = referenceSpeed;
    }

    /**
     * @return The speed pattern identifier in backward direction
     */
    public SpeedPatternIdentifier reverseSpeedPatternIdentifier()
    {
        return reverseSpeedPatternIdentifier;
    }

    public void reverseSpeedPatternIdentifier(final SpeedPatternIdentifier reversedSpeedPatternIdentifier)
    {
        reverseSpeedPatternIdentifier = reversedSpeedPatternIdentifier;
    }

    public void reverseTmcIdentifiers(final Iterable<RoadSectionIdentifier> reverseTmcIdentifiers)
    {
        this.reverseTmcIdentifiers = reverseTmcIdentifiers;
    }

    @Override
    public HeavyWeightEdge reversed()
    {
        final var reversed = graph().newHeavyWeightEdge(identifier());
        reversed.identifier(identifier().reversed().asLong());
        reversed.from(to());
        reversed.to(from());
        reversed.fromNodeIdentifier(toNodeIdentifier());
        reversed.toNodeIdentifier(fromNodeIdentifier());
        reversed.toVertexClipped(isFromVertexClipped());
        reversed.fromVertexClipped(isToVertexClipped());
        reversed.referenceSpeed(reverseReferenceSpeed);
        reversed.uniDbReverseReferenceSpeed(referenceSpeed);
        reversed.speedPatternIdentifier(reverseSpeedPatternIdentifier);
        reversed.reverseSpeedPatternIdentifier(speedPatternIdentifier);
        reversed.roadShape(roadShape().reversed());
        reversed.tmcIdentifiers(reverseTmcIdentifiers());
        reversed.reverseTmcIdentifiers(tmcIdentifiers());
        reversed.toGradeSeparation(fromGradeSeparation());
        reversed.fromGradeSeparation(toGradeSeparation());
        return reversed;
    }

    @Override
    public RoadFunctionalClass roadFunctionalClass()
    {
        assert roadFunctionalClass != null;
        return roadFunctionalClass;
    }

    public void roadFunctionalClass(final RoadFunctionalClass roadFunctionalClass)
    {
        assert roadFunctionalClass != null;
        this.roadFunctionalClass = roadFunctionalClass;
    }

    @Override
    public List<RoadName> roadNames(final RoadName.Type type)
    {
        var roadNames = this.roadNames.get(type);
        if (roadNames == null)
        {
            roadNames = Collections.emptyList();
        }
        return roadNames;
    }

    public void roadNames(final RoadName.Type type, final List<RoadName> roadNames)
    {
        assert roadNames.stream().noneMatch(Objects::isNull);
        this.roadNames.put(type, roadNames);
    }

    @Override
    @TdkExcludeProperty
    public Polyline roadShape()
    {
        if (roadShape == null)
        {
            return Polyline.fromLocations(fromLocation(), toLocation());
        }
        return roadShape;
    }

    public void roadShape(final Polyline roadShape)
    {
        this.roadShape = roadShape;
        if (roadShape != null)
        {
            bounds = roadShape.bounds();
        }
    }

    public void roadShapeAndLength(final Polyline shape, final Location from, final Location to)
    {
        if (shape != null)
        {
            roadShape(shape);
            fromLocation(shape.start());
            toLocation(shape.end());
            length(shape.length());
        }
        else
        {
            fromLocation(from);
            toLocation(to);
            length(from.distanceTo(to));
        }
    }

    @Override
    public RoadState roadState()
    {
        return roadState;
    }

    public void roadState(final RoadState roadState)
    {
        this.roadState = roadState;
    }

    @Override
    public RoadSubType roadSubType()
    {
        return roadSubType;
    }

    public void roadSubType(final RoadSubType roadSubType)
    {
        this.roadSubType = roadSubType;
    }

    @Override
    public RoadSurface roadSurface()
    {
        return surface;
    }

    @Override
    public RoadType roadType()
    {
        return roadType;
    }

    public void roadType(final RoadType roadType)
    {
        this.roadType = roadType;
    }

    @Override
    public Speed speedLimit()
    {
        return speedLimit;
    }

    public void speedLimit(final Speed speedLimit)
    {
        this.speedLimit = Maybe.apply(speedLimit, speed -> speed.minimum(Speed.kilometersPerHour(160)));
    }

    /**
     * @return The speed pattern identifier in forward direction
     */
    @Override
    public SpeedPatternIdentifier speedPatternIdentifier()
    {
        return speedPatternIdentifier;
    }

    public void speedPatternIdentifier(final SpeedPatternIdentifier speedPatternIdentifier)
    {
        this.speedPatternIdentifier = speedPatternIdentifier;
    }

    @Override
    public State state()
    {
        return state;
    }

    public void state(final State state)
    {
        this.state = state;
    }

    public void surface(final RoadSurface surface)
    {
        this.surface = surface;
    }

    @Override
    public PbfTagList tagList()
    {
        return tags;
    }

    public void tags(final PbfTagList tags)
    {
        assert tags.isValid();
        this.tags = tags;
    }

    public Polyline testPolyline()
    {
        return Polyline.fromLocations(
                Location.TELENAV_HEADQUARTERS.moved(Heading.NORTHEAST, Distance._100_METERS),
                Location.TELENAV_HEADQUARTERS);
    }

    @Override
    public Iterable<RoadSectionIdentifier> tmcIdentifiers()
    {
        return Objects.requireNonNullElse(tmcIdentifiers, Collections.emptyList());
    }

    public void tmcIdentifiers(final Iterable<RoadSectionIdentifier> tmcIdentifiers)
    {
        this.tmcIdentifiers = tmcIdentifiers;
    }

    @Override
    public Vertex to()
    {
        return to;
    }

    public void to(final Vertex to)
    {
        this.to = to;
        if (to != null)
        {
            toLocation = to.location();
        }
    }

    @Override
    public GradeSeparation toGradeSeparation()
    {
        return toGradeSeparation;
    }

    public void toGradeSeparation(final GradeSeparation separation)
    {
        // Grade separation can legitimately be null here because we don't store the omnipresent
        // grade separation "GROUND"
        toGradeSeparation = separation == null ? GradeSeparation.GROUND : separation;
    }

    @Override
    public Location toLocation()
    {
        return toLocation;
    }

    public void toLocation(final Location toLocation)
    {
        assert toLocation != null;
        this.toLocation = toLocation;
    }

    @Override
    public NodeIdentifier toNodeIdentifier()
    {
        return toNodeIdentifier;
    }

    public void toNodeIdentifier(final NodeIdentifier identifier)
    {
        toNodeIdentifier = identifier;
    }

    public void toVertexClipped(final Boolean toVertexClipped)
    {
        this.toVertexClipped = toVertexClipped;
    }

    @Override
    @TdkExcludeProperty
    public VertexIdentifier toVertexIdentifier()
    {
        return to != null ? to.identifier() : null;
    }

    public void tollRoad(final Boolean isTollRoad)
    {
        this.isTollRoad = isTollRoad;
    }

    public void traceCount(final Count traceCount)
    {
        this.traceCount = traceCount;
    }

    @Override
    public Duration travelTime()
    {
        return freeFlowSpeed().average().timeToTravel(length());
    }

    @Override
    public Type type()
    {
        return type;
    }

    public void type(final Type type)
    {
        this.type = type;
    }

    public void underConstruction(final Boolean isUnderConstruction)
    {
        this.isUnderConstruction = isUnderConstruction;
    }

    /**
     * @return The reference speed (used for historical speed calculation) in forward direction
     */
    @Override
    public Speed uniDbReferenceSpeed()
    {
        return referenceSpeed;
    }

    /**
     * @return The reference speed (used for historical speed calculation) in backward direction
     */
    public Speed uniDbReverseReferenceSpeed()
    {
        return reverseReferenceSpeed;
    }

    public void uniDbReverseReferenceSpeed(final Speed reversedReferenceSpeed)
    {
        reverseReferenceSpeed = reversedReferenceSpeed;
    }

    protected Iterable<RoadSectionIdentifier> reverseTmcIdentifiers()
    {
        return Objects.requireNonNullElse(reverseTmcIdentifiers, Collections.emptyList());
    }
}
