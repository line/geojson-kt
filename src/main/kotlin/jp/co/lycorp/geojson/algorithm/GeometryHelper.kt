package jp.co.lycorp.geojson.algorithm

import jp.co.lycorp.geojson.Edge
import jp.co.lycorp.geojson.LinearRing
import jp.co.lycorp.geojson.Position

/**
 * Checks whether a given value lies within the range defined by two bounds, inclusive.
 */
fun isBetween(value: Double, bound1: Double, bound2: Double): Boolean {
    return (minOf(bound1, bound2)) <= value && value <= maxOf(bound1, bound2)
}

/**
 *　Function implementing the ray casting algorithm
 *
 *　The idea is to cast a ray from the point towards infinity
 * and count how many times it intersects with the edges of the polygon.
 * [wikipedia](https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm)
 *
 * Return -1 if there is a point on the edge
 *
 * [Referenced code](https://medium.com/@girishajmera/exploring-algorithms-to-determine-points-inside-or-outside-a-polygon-038952946f87)
 *
 */

fun rayCasting(point: Position, polygon: List<Position>): Int {
    val numVertices = polygon.size
    val (lng, lat) = point
    var crossings = 0
    for (i in 0 until numVertices) {
        val (lng1, lat1) = polygon[i]
        val (lng2, lat2) = polygon[(i + 1) % numVertices]
        if (Edge(Position(lng1, lat1), Position(lng2, lat2)).isCollinear(point)) return -1
        if ((minOf(lat1, lat2) < lat && lat < maxOf(lat1, lat2)) &&
            (lng <= maxOf(lng1, lng2))
        ) {
            val intersectionX = (lat - lat1) * (lng2 - lng1) / (lat2 - lat1) + lng1
            if (lng1 == lng2 || lng <= intersectionX) {
                crossings++
            }
        }
    }
    return crossings
}

/**
 * Calculate polygon centroid
 *
 * [Reference site](https://mathworld.wolfram.com/PolygonCentroid.html)
 */
fun calculatePolygonCentroid(linearRing: LinearRing): Position {
    val vertices = linearRing.coordinates
    var signedArea = 0.0
    var centroidX = 0.0
    var centroidY = 0.0

    for (i in 0 until vertices.size - 1) {
        val x0 = vertices[i].lng
        val y0 = vertices[i].lat
        val x1 = vertices[i + 1].lng
        val y1 = vertices[i + 1].lat

        val area = x0 * y1 - x1 * y0
        signedArea += area
        centroidX += (x0 + x1) * area
        centroidY += (y0 + y1) * area
    }

    signedArea *= 0.5
    require(signedArea > 0.0) {
        "The area of the polygon is zero."
    }

    centroidX /= (6.0 * signedArea)
    centroidY /= (6.0 * signedArea)

    return Position(centroidX, centroidY)
}

/**
 * Determines if two convex polygons intersect.
 *
 * [Referenced code](https://tjkendev.github.io/procon-library/python/geometry/convex_polygons_intersection.html)
 *
 */
fun convexPolygonsIntersection(linearRing1: List<Position>, linearRing2: List<Position>): Boolean {
    val vertices1 = linearRing1.dropLast(1)
    val vertices2 = linearRing2.dropLast(1)
    val vertexCount1 = vertices1.size
    val vertexCount2 = vertices2.size

    var index1 = 0
    var index2 = 0

    while (index1 < vertexCount1 && index2 < vertexCount2) {
        val previousVertex1 = vertices1[(index1 - 1 + vertexCount1) % vertexCount1]
        val currentVertex1 = vertices1[index1]
        val previousVertex2 = vertices2[(index2 - 1 + vertexCount2) % vertexCount2]
        val currentVertex2 = vertices2[index2]

        if (Edge(previousVertex1, currentVertex1).isIntersected(Edge(previousVertex2, currentVertex2))) {
            return true
        }

        val direction = determineDirection(previousVertex1, currentVertex1, previousVertex2, currentVertex2)
        if (direction.first == 0 && direction.second == 0) {
            break
        }
        index1 += direction.first
        index2 += direction.second
    }

    return false
}

private fun determineDirection(
    previousVertex1: Position,
    currentVertex1: Position,
    previousVertex2: Position,
    currentVertex2: Position,
): Pair<Int, Int> {
    val deltaX1 = currentVertex1.lng - previousVertex1.lng
    val deltaY1 = currentVertex1.lat - previousVertex1.lat
    val deltaX2 = currentVertex2.lng - previousVertex2.lng
    val deltaY2 = currentVertex2.lat - previousVertex2.lat
    val crossProduct = deltaX1 * deltaY2 - deltaX2 * deltaY1
    val crossVa = calculateCrossProduct(previousVertex2, currentVertex2, currentVertex1)
    val crossVb = calculateCrossProduct(previousVertex1, currentVertex1, currentVertex2)

    return when {
        crossProduct == 0.0 && crossVa < 0 && crossVb < 0 -> Pair(0, 0) // parallel and outside
        crossProduct == 0.0 && crossVa == 0.0 && crossVb == 0.0 -> Pair(1, 0) // on the same line
        crossProduct >= 0 -> if (crossVb > 0) Pair(1, 0) else Pair(0, 1) // intersection direction
        else -> if (crossVa > 0) Pair(0, 1) else Pair(1, 0) // intersection direction
    }
}

/**
* Calculates the dot product relative to the origin.
*/
fun calculateDotProduct(origin: Position, pointA: Position, pointB: Position): Double {
    val (ox, oy) = origin
    val (ax, ay) = pointA
    val (bx, by) = pointB
    return (ax - ox) * (bx - ox) + (ay - oy) * (by - oy)
}

/**
 * Calculates the cross product relative to the origin.
 */
fun calculateCrossProduct(origin: Position, pointA: Position, pointB: Position): Double {
    val (ox, oy) = origin
    val (ax, ay) = pointA
    val (bx, by) = pointB
    return (ax - ox) * (by - oy) - (bx - ox) * (ay - oy)
}

/**
 * Calculates the squared distance between two points.
 */
fun calculateSquareDist(pointA: Position, pointB: Position): Double {
    val (ax, ay) = pointA
    val (bx, by) = pointB
    return (ax - bx) * (ax - bx) + (ay - by) * (ay - by)
}
