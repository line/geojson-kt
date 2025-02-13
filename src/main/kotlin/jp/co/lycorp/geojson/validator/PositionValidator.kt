package jp.co.lycorp.geojson.validator

import jp.co.lycorp.geojson.Position

/**
 * LineStringValidator class
 *
 * Validates whether a given Position is valid
 */
internal object PositionValidator {
    private const val MINIMUM_LONGITUDE = -180.0
    private const val MAXIMUM_LONGITUDE = 180.0
    private const val MINIMUM_LATITUDE = -90.0
    private const val MAXIMUM_LATITUDE = 90.0
    fun validate(position: Position) {
        require(position.lng in MINIMUM_LONGITUDE..MAXIMUM_LONGITUDE) {
            "Longitude must be between -180 and 180 degrees"
        }
        require(position.lat in MINIMUM_LATITUDE..MAXIMUM_LATITUDE) {
            "Latitude must be between -90 and 90 degrees"
        }
    }
}
