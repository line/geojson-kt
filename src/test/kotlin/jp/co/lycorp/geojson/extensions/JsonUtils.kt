package jp.co.lycorp.geojson.extensions

/**
 * Class containing a collection of utility functions for Json
 */
internal object JsonUtils {
    /**
     * Converts the string to a compacted JSON format.
     *
     * This function removes indentation and whitespace
     * from the string to produce a compact JSON representation.
     */
    fun String.toCompactedJson(): String {
        return this.replace("\\s".toRegex(), "")
    }
}
