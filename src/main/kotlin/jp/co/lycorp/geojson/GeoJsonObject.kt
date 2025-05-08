package jp.co.lycorp.geojson

import com.fasterxml.jackson.annotation.JsonInclude

/**
 *
 * GeoJsonObject class
 *
 * Base class
 *
 * @property type Type that GeoJSON has
 * @property bbox Information on the coordinate range for its Geometries, Features, or FeatureCollections
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class GeoJsonObject(
    val type: String,
    open val bbox: BBox? = null,
)
