package jp.co.lycorp.geojson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 *
 * GeoJsonObject class
 *
 * Base class
 *
 * @property type Type that GeoJSON has
 * @property bbox Information on the coordinate range for its Geometries, Features, or FeatureCollections
 */
@JsonTypeInfo(property = "type", use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(Point::class, name = "Point"),
    JsonSubTypes.Type(MultiPoint::class, name = "MultiPoint"),
    JsonSubTypes.Type(LineString::class, name = "LineString"),
    JsonSubTypes.Type(MultiLineString::class, name = "MultiLineString"),
    JsonSubTypes.Type(Polygon::class, name = "Polygon"),
    JsonSubTypes.Type(MultiPolygon::class, name = "MultiPolygon"),
    JsonSubTypes.Type(Feature::class, name = "Feature"),
    JsonSubTypes.Type(FeatureCollection::class, name = "FeatureCollection"),
)
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class GeoJsonObject(
    val type: String,
    open val bbox: BBox? = null,
)
