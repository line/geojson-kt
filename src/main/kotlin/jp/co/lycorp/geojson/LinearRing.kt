package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.algorithm.calculatePolygonCentroid
import jp.co.lycorp.geojson.algorithm.rayCasting

typealias LinearRingCoordinates = List<Position>

/**
 * LineString class
 *
 * linear ring is a closed LineString with four or more positions.
 *
 * @property coordinates An Array of Position
 */
data class LinearRing(val coordinates: LinearRingCoordinates) {
    init {
        require(coordinates.isNotEmpty()) {
            "LinearRing cannot be empty"
        }
    }

    /**
     * Determine if a given point is on an edge of this LinearRing
     */
    fun isOnEdge(position: Position): Boolean {
        return coordinates.windowed(2).any {
                (s, t) ->
            Edge(s, t).isCollinear(position)
        }
    }

    /**
     * Determine if a given point is inside this linearRing
     *
     * Returns false if it is on an edge
     */
    fun isPointInLinearRing(position: Position): Boolean {
        return rayCasting(position, coordinates) % 2 == 1
    }

    /**
     * Determine if a given point is outside this linearRing
     *
     * Returns false if it is on an edge
     */
    fun isPointOutsideLinearRing(position: Position): Boolean {
        return rayCasting(position, coordinates) % 2 == 0
    }

    /**
     * Calculate polygon centroid
     */
    fun calculateCentroid(): Position {
        return calculatePolygonCentroid(this)
    }
}
