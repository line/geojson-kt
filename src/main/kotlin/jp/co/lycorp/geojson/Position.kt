package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.validator.PositionValidator

/**
 * Position class
 *
 * Class that stores GeoJSON Position information
 *
 * @property lng Longitude
 * @property lat Latitude
 * @property alt Altitude (optional)
 */
data class Position(
    val lng: Double,
    val lat: Double,
    val alt: Double? = null,
) {
    init {
        PositionValidator.validate(this)
    }
}
