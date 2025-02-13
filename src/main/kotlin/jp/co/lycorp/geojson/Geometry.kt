package jp.co.lycorp.geojson

/**
 * Geometry class
 *
 * Base class for GeometryObject
 *
 * @param type Type member of GeometryObject
 * @property coordinates Coordinates array
 */
abstract class Geometry<out T>(type: String) : GeoJsonObject(type) {
    /**
     * Coordinate array
     * The structure of the elements in this array is determined by the type of geometry.
     */
    abstract val coordinates: T?

    /**
     * Derive BBox from coordinates
     */
    abstract fun calculateBBox(): BBox
}
