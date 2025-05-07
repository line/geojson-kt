package jp.co.lycorp.geojson

/**
 * Feature class
 *
 * Class that stores GeoJSON Feature Object information
 *
 * @property geometry Geometry object
 * @property properties Any JSON object
 * @property id Identifier. Numeric or string.
 */
data class Feature(
    val geometry: Geometry<*>,
    override val bbox: BBox? = null,
    val properties: Map<String, Any>? = null,
    val id: FeatureId? = null,
) : GeoJsonObject("Feature") {
    /**
     * Derive BBox from coordinates
     */
    fun calculateBBox(): BBox {
        return geometry.calculateBBox()
    }
}
