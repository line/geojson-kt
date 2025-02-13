package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.algorithm.calculateCrossProduct
import jp.co.lycorp.geojson.algorithm.calculateDotProduct
import jp.co.lycorp.geojson.algorithm.calculateSquareDist
import jp.co.lycorp.geojson.algorithm.isBetween

/**
 * Edge class
 *
 * Edge with start and end points
 *
 * @property startPosition Edge start point
 * @property endPosition Edge end point
 */
data class Edge(val startPosition: Position, val endPosition: Position) {
    /**
     * Determine if a given point is collinear with the points in this edge
     *
     * [Cross-product](https://en.wikipedia.org/wiki/Cross_product) is used
     * to determine if the three points are on the same line.
     */
    fun isCollinear(point: Position): Boolean {
        val (pointLng, pointLat) = point
        val (startLng, startLat) = startPosition
        val (endLng, endLat) = endPosition

        val isCollinear =
            (endLat - startLat) * (pointLng - startLng) == (endLng - startLng) * (pointLat - startLat)
        return isCollinear &&
            isBetween(pointLng, startLng, endLng) &&
            isBetween(pointLat, startLat, endLat)
    }

    /**
     * Determines if two line segments intersect.
     */
    fun isIntersected(otherEdge: Edge): Boolean {
        val otherStartPosition = otherEdge.startPosition
        val otherEndPosition = otherEdge.endPosition
        val crossProduct1 = calculateCrossProduct(otherStartPosition, otherEndPosition, startPosition)
        val crossProduct2 = calculateCrossProduct(otherStartPosition, otherEndPosition, endPosition)

        // handle collinear edges
        if (crossProduct1 == 0.0 && crossProduct2 == 0.0) {
            var distanceToStart = calculateDotProduct(otherStartPosition, otherEndPosition, startPosition)
            var distanceToEnd = calculateDotProduct(otherStartPosition, otherEndPosition, endPosition)
            if (distanceToStart > distanceToEnd) {
                distanceToStart = distanceToEnd.also { distanceToEnd = distanceToStart }
            }
            return 0 <= distanceToEnd &&
                distanceToStart <= calculateSquareDist(otherStartPosition, otherEndPosition)
        }

        val crossProduct3 = calculateCrossProduct(startPosition, endPosition, otherStartPosition)
        val crossProduct4 = calculateCrossProduct(startPosition, endPosition, otherEndPosition)
        return crossProduct1 * crossProduct2 <= 0 && crossProduct3 * crossProduct4 <= 0
    }
}
