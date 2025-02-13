package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.validator.LineStringValidator

typealias MultiLineStringCoordinates = List<List<Position>>

/**
 * MultiLineString class
 *
 * Class that stores GeoJSON MultiLineString information
 *
 * @property coordinates An Array of Position arrays
 */
data class MultiLineString(
    override val coordinates: MultiLineStringCoordinates,
    override val bbox: BBox? = null,
) : Geometry<MultiLineStringCoordinates>("MultiLineString") {
    init {
        coordinates.forEach {
            LineStringValidator.validate(it)
        }
    }
    constructor(vararg lineStrings: LineString) :
        this(
            lineStrings.map { it.coordinates },
            BBox.from(lineStrings.map { it.coordinates }.flatten()),
        )

    /**
     * Split MultiLineString into LineString lists.
     */
    fun split(): List<LineString> {
        return coordinates.map {
            LineString(it, BBox.from(it))
        }
    }

    /**
     * Returns a new MultiLineString with the passed LineString appended.
     */
    fun added(vararg lineStrings: LineString): MultiLineString {
        val newCoordinates: List<List<Position>> = coordinates + lineStrings.map { it.coordinates }
        val newBBox = BBox.from(newCoordinates.flatten())
        return MultiLineString(newCoordinates, newBBox)
    }

    override fun calculateBBox(): BBox {
        return BBox.from(coordinates.flatten())
    }
}
