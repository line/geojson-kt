package jp.co.lycorp.geojson

/**
 * Sealed class for feature ID.
 * The value is either a JSON string or number.
 */
sealed class FeatureId {
    companion object {
        /**
         * Generates and returns a class for the [FeatureId] corresponding to the specified value.
         *
         * @param value Raw value in JSON
         * @return Valid FeatureId instance
         */
        fun of(value: Any): FeatureId {
            return when (value) {
                is String -> {
                    StringFeatureId(value)
                }

                is Number -> {
                    NumberFeatureId(value)
                }

                else -> {
                    throw IllegalArgumentException("invalid value as feature ID: $value")
                }
            }
        }
    }
}

/**
 * Feature ID whose value is a JSON string
 *
 * @property value Raw value
 */
data class StringFeatureId(val value: String) : FeatureId()

/**
 * Feature ID whose value is a JSON number
 *
 * @property value Raw value
 */
data class NumberFeatureId(val value: Number) : FeatureId()
