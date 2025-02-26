package jp.co.lycorp.geojson.validator

import jp.co.lycorp.geojson.PolygonCoordinates

/**
 * PolygonValidator class
 *
 * Validates whether a given Polygon is valid
 *
 */
internal object PolygonValidator {
    fun validate(coordinates: PolygonCoordinates) {
        require(coordinates.isNotEmpty()) { "Polygon coordinates must have at least one linear ring" }

        coordinates.forEach { LinearRingValidator.validate(it) }
        if (coordinates.size >= 2) {
            RingIntersectionValidator.validateRings(coordinates)
        }
    }
}
