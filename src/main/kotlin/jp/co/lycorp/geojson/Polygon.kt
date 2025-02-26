package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.validator.PolygonValidator

typealias PolygonCoordinates = List<List<Position>>

/**
 * Polygon class
 *
 * Class that stores GeoJSON Polygon information
 *
 * @property coordinates An array of Position arrays
 */
data class Polygon(
    override val coordinates: PolygonCoordinates,
    override val bbox: BBox? = null,
) : Geometry<PolygonCoordinates>("Polygon"), Surface {
    init {
        PolygonValidator.validate(coordinates)
    }

    /**
     * Determines whether a given position is inside the polygon
     * If it is on an edge of the polygon, it is considered to be inside when `allowOnEdge` is true.
     */
    fun contains(position: Position, allowOnEdge: Boolean = true): Boolean {
        val isPointInExteriorRing = LinearRing(coordinates[0]).isPointInLinearRing(position)
        val isPointOutsideInteriorRing = coordinates.drop(1).all {
            LinearRing(it).isPointOutsideLinearRing(position)
        }
        return if (allowOnEdge) {
            val isOnEdge = coordinates.any {
                LinearRing(it).isOnEdge(position)
            }
            (isPointInExteriorRing && isPointOutsideInteriorRing) || isOnEdge
        } else {
            isPointInExteriorRing && isPointOutsideInteriorRing
        }
    }

    /**
     * Calculate Exterior Linear Ring centroid
     */
    fun calculateCentroid(): Position {
        return LinearRing(coordinates[0]).calculateCentroid()
    }

    override fun calculateBBox(): BBox {
        return BBox.from(coordinates.first().dropLast(1))
    }

    override fun contains(point: Point, allowOnEdge: Boolean): Boolean =
        contains(point.coordinates, allowOnEdge)
}
