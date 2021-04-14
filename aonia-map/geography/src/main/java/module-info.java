open module aonia.map.geography
{
    requires transitive java.desktop;

    requires transitive kivakit.data.compression;
    requires transitive kivakit.core.resource;
    requires transitive kivakit.math;

    requires transitive aonia.map.measurements;

    exports com.telenav.aonia.map.geography.indexing.polygon;
    exports com.telenav.aonia.map.geography.indexing.quadtree;
    exports com.telenav.aonia.map.geography.indexing.rtree;
    exports com.telenav.aonia.map.geography.indexing.segment;
    exports com.telenav.aonia.map.geography.shape.polyline.compression.huffman;
    exports com.telenav.aonia.map.geography.shape.polyline.compression.differential;
    exports com.telenav.aonia.map.geography.shape.polyline;
    exports com.telenav.aonia.map.geography.project;
    exports com.telenav.aonia.map.geography.shape.rectangle;
    exports com.telenav.aonia.map.geography.shape.segment;
    exports com.telenav.aonia.map.geography.shape;
    exports com.telenav.aonia.map.geography;
}
