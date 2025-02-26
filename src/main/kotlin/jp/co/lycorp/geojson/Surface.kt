package jp.co.lycorp.geojson

/**
 * A flat, two-dimensional figure (that may be curved).
 * [Reference](https://en.wikipedia.org/wiki/Surface_(mathematics))
 *
 * Polygons and MultiPolygons can be considered types of surfaces.
 * [RFC7946](https://tex2e.github.io/rfc-translater/html/rfc7946.html#1--Introduction)
 */
interface Surface {
    /**
     * Determines whether a given point is inside the surface's boundaries.
     * If it is on an edge of the surface, it is considered to be inside when `allowOnEdge` is true.
     *
     * Note that altitude is ignored; this is a purely 2D calculation.
     */
    fun contains(point: Point, allowOnEdge: Boolean = true): Boolean
}
