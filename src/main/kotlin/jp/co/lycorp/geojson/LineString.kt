package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.validator.LineStringValidator

typealias LineStringCoordinates = List<Position>

/**
 * LineString class
 *
 * Class that stores GeoJSON LineString information
 *
 * @property coordinates An Array of Position
 */
data class LineString(
    override val coordinates: LineStringCoordinates,
    override val bbox: BBox? = null,
) : Geometry<LineStringCoordinates>("LineString") {
    init {
        LineStringValidator.validate(coordinates)
    }

    override fun calculateBBox(): BBox {
        return BBox.from(coordinates)
    }
}
