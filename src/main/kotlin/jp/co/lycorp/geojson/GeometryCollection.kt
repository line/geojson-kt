package jp.co.lycorp.geojson

/**
 * GeometryCollection class
 *
 * Class that stores GeoJSON GeometryCollection information
 *
 * @property geometries Geometry array
 */
data class GeometryCollection(
    val geometries: List<Geometry<*>>,
    override val bbox: BBox? = null,
) : GeoJsonObject("GeometryCollection"), Iterable<Geometry<*>> {
    /**
     * Returns an iterator for `Geometry<*>` based on the `geometries` property of this class.
     */
    override fun iterator(): Iterator<Geometry<*>> = geometries.iterator()
}
