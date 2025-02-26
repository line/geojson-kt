package jp.co.lycorp.geojson.validator

import jp.co.lycorp.geojson.LineStringCoordinates

/**
 * LineStringValidator class
 *
 * Validates whether a given LineString is valid
 */
internal object LineStringValidator {
    private const val MINIMUM_SIZE_OF_THE_COORDINATE_ARRAY = 2

    fun validate(coordinates: LineStringCoordinates) {
        require(coordinates.size >= MINIMUM_SIZE_OF_THE_COORDINATE_ARRAY) {
            "LineString coordinates must have at least 2 Positions"
        }
    }
}
