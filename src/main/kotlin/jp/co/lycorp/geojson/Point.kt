package jp.co.lycorp.geojson

typealias PointCoordinates = Position

/**
 * Point class
 *
 * Class that stores GeoJSON Point information
 *
 * @property coordinates It is single position
 */
data class Point(
    override val coordinates: PointCoordinates,
    override val bbox: BBox? = null,
) : Geometry<PointCoordinates>("Point") {

    /**
     * Determines whether the point is inside the boundaries of a surface.
     * If it is on an edge of the surface, it is considered to be inside when `allowOnEdge` is true.
     *
     * Note that altitude is ignored; this is a purely 2D calculation.
     */
    fun isIn(surface: Surface, allowOnEdge: Boolean = true): Boolean = surface.contains(this, allowOnEdge)

    /**
     * Determines whether the point is on the specified edge.
     */
    fun isOn(edge: Edge): Boolean = edge.isCollinear(this.coordinates)

    override fun calculateBBox(): BBox {
        return BBox.from(listOf(coordinates))
    }
}
