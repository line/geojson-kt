package jp.co.lycorp.geojson.validator

import jp.co.lycorp.geojson.LinearRingCoordinates

/**
 * LinearRingValidator class
 *
 * Validates whether a given LinearRing is valid
 *
 */
internal object LinearRingValidator {
    private const val MINIMUM_SIZE_OF_THE_COORDINATE_ARRAY = 4
    private const val MINIMUM_NUMBER_OF_UNIQUE_NODES = 3

    fun validate(coordinates: LinearRingCoordinates) {
        require(coordinates.size >= MINIMUM_SIZE_OF_THE_COORDINATE_ARRAY) {
            "Polygon linear ring must have at least 4 coordinates"
        }
        require(coordinates.first() == coordinates.last()) {
            "Polygon linear ring first element must be equal to last element"
        }
        require(coordinates.distinct().size >= MINIMUM_NUMBER_OF_UNIQUE_NODES) {
            "Polygon linear ring must have at least 3 unique coordinates"
        }
    }
}
