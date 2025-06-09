package jp.co.lycorp.geojson

/**
 * BBox class
 *
 * Class that stores GeoJSON BBox information
 *
 * @property minLng Longitude of the most southwesterly point
 * @property minLat Latitude of the most southwesterly point
 * @property maxLng Longitude of the most northeasterly point
 * @property maxLat Latitude of the most northeasterly point
 * @property minAlt Altitude of the most southwesterly point (optional)
 * @property maxAlt Altitude of the most northeasterly point (optional)
 */
data class BBox(
    val minLng: Double,
    val minLat: Double,
    val maxLng: Double,
    val maxLat: Double,
    val minAlt: Double? = null,
    val maxAlt: Double? = null,
) {
    companion object {
        /**
         * Function to derive BBox from an array of Positions
         */
        fun from(coordinates: List<Position>): BBox {
            require(coordinates.isNotEmpty()) { "The provided list is empty. Please provide a non-empty list." }

            return coordinates.asSequence()
                .map { BBox(it.lng, it.lat, it.lng, it.lat, it.alt, it.alt) }
                .reduce { acc, bbox ->
                    val minAlt = when {
                        bbox.minAlt == null -> acc.minAlt
                        acc.minAlt != null -> minOf(bbox.minAlt, acc.minAlt)
                        else -> bbox.minAlt
                    }

                    val maxAlt = when {
                        bbox.maxAlt == null -> acc.maxAlt
                        acc.maxAlt != null -> maxOf(bbox.maxAlt, acc.maxAlt)
                        else -> bbox.maxAlt
                    }

                    BBox(
                        minOf(acc.minLng, bbox.minLng),
                        minOf(acc.minLat, bbox.minLat),
                        maxOf(acc.maxLng, bbox.maxLng),
                        maxOf(acc.maxLat, bbox.maxLat),
                        minAlt,
                        maxAlt,
                    )
                }
        }
    }
}
