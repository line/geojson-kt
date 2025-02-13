package jp.co.lycorp.geojson

typealias MultiPointCoordinates = List<Position>

/**
 * MultiPoint class
 *
 * Class that stores GeoJSON MultiPoint information
 *
 * @property coordinates An Array of Position
 */
data class MultiPoint(
    override val coordinates: MultiPointCoordinates,
    override val bbox: BBox? = null,
) : Geometry<MultiPointCoordinates>("MultiPoint") {
    constructor(vararg points: Point) :
        this(points.map { it.coordinates }, BBox.from(points.map { it.coordinates }))

    /**
     * Split MultiPoint into Point lists.
     */
    fun split(): List<Point> {
        return coordinates.map {
            Point(it)
        }
    }

    /**
     * Returns a new MultiPoint with the passed Point appended.
     */
    fun added(vararg points: Point): MultiPoint {
        val newCoordinates = coordinates + points.map { it.coordinates }
        val newBBox = BBox.from(newCoordinates)
        return MultiPoint(newCoordinates, newBBox)
    }

    override fun calculateBBox(): BBox {
        return BBox.from(coordinates)
    }
}
