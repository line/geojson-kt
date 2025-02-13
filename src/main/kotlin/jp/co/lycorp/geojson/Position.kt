package jp.co.lycorp.geojson

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jp.co.lycorp.geojson.jackson.PositionDeserializer
import jp.co.lycorp.geojson.jackson.PositionSerializer
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
@JsonDeserialize(using = PositionDeserializer::class)
@JsonSerialize(using = PositionSerializer::class)
data class Position(
    val lng: Double,
    val lat: Double,
    val alt: Double? = null,
) {
    init {
        PositionValidator.validate(this)
    }
}
