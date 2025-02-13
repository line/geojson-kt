package jp.co.lycorp.geojson

/**
 * Feature collection class
 *
 * Class that stores GeoJSON FeatureCollection information
 *
 * @property features Array of Feature objects
 */
data class FeatureCollection(
    val features: List<Feature>,
    override val bbox: BBox? = null,
) : GeoJsonObject("FeatureCollection"), Iterable<Feature> {
    /**
     * Returns an iterator for `Feature` based on the `features` property of this class.
     */
    override fun iterator(): Iterator<Feature> = features.iterator()

    /**
     * Derive BBox from coordinates
     */
    fun calculateBBox(): BBox {
        return BBox(
            features.minOf { it.calculateBBox().minLng },
            features.minOf { it.calculateBBox().minLat },
            features.maxOf { it.calculateBBox().maxLng },
            features.maxOf { it.calculateBBox().maxLat },
        )
    }
}
