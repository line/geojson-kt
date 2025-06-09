package jp.co.lycorp.geojson

/**
 *
 * GeoJsonObject class
 *
 * Base class
 *
 * @property type Type that GeoJSON has
 * @property bbox Information on the coordinate range for its Geometries, Features, or FeatureCollections
 */
abstract class GeoJsonObject(
    val type: String,
    open val bbox: BBox? = null,
)
