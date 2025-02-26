package jp.co.lycorp.geojson

import jp.co.lycorp.geojson.validator.PolygonValidator

typealias MultiPolygonCoordinates = List<List<List<Position>>>

/**
 * MultiPolygon class
 *
 * Class that stores GeoJSON MultiPolygon information
 *
 * @property coordinates An array of arrays of Position arrays
 */
data class MultiPolygon(
    override val coordinates: MultiPolygonCoordinates,
    override val bbox: BBox? = null,
) : Geometry<MultiPolygonCoordinates>("MultiPolygon"), Surface {
    init {
        coordinates.forEach {
            PolygonValidator.validate(it)
        }
    }
    constructor(vararg polygons: Polygon) :
        this(
            polygons.map { it.coordinates },
            BBox.from(polygons.flatMap { it.coordinates.first().dropLast(1) }),
        )

    /**
     * Split MultiPolygon into Polygon lists.
     */
    fun split(): List<Polygon> {
        return coordinates.map {
            Polygon(it, BBox.from(it.first()))
        }
    }

    /**
     * Returns a new MultiPolygon with the passed Polygon appended.
     */
    fun added(vararg polygons: Polygon): MultiPolygon {
        val newCoordinates: List<List<List<Position>>> = coordinates + polygons.map { it.coordinates }
        val newBBox = BBox.from(newCoordinates.flatMap { it.first().dropLast(1) })
        return MultiPolygon(newCoordinates, newBBox)
    }

    override fun calculateBBox(): BBox {
        return BBox.from(coordinates.flatMap { it.first().dropLast(1) })
    }

    override fun contains(point: Point, allowOnEdge: Boolean): Boolean {
        return this.split().any {
            it.contains(point, allowOnEdge)
        }
    }
}
