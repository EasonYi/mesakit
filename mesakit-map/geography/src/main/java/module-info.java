open module mesakit.map.geography
{
    // MesaKit
    requires transitive mesakit.map.measurements;

    // KivaKit
    requires transitive kivakit.serialization.kryo;
    requires transitive kivakit.math;
    requires transitive kivakit.resource;
    requires transitive kivakit.primitive.collections;

    // Annotations
    requires org.jetbrains.annotations;

    // Geographic
    requires google.polyline.codec;

    // Java
    requires transitive java.desktop;

    exports com.telenav.mesakit.map.geography.indexing.polygon;
    exports com.telenav.mesakit.map.geography.indexing.quadtree;
    exports com.telenav.mesakit.map.geography.indexing.rtree;
    exports com.telenav.mesakit.map.geography.indexing.segment;
    exports com.telenav.mesakit.map.geography.shape.polyline.compression.differential;
    exports com.telenav.mesakit.map.geography.shape.polyline;
    exports com.telenav.mesakit.map.geography.projection;
    exports com.telenav.mesakit.map.geography.shape.rectangle;
    exports com.telenav.mesakit.map.geography.shape.segment;
    exports com.telenav.mesakit.map.geography.shape;
    exports com.telenav.mesakit.map.geography;
    exports com.telenav.mesakit.map.geography.projection.projections;
}
