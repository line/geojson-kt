package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jp.co.lycorp.geojson.Feature
import jp.co.lycorp.geojson.FeatureCollection
import jp.co.lycorp.geojson.GeoJsonObject
import jp.co.lycorp.geojson.LineString
import jp.co.lycorp.geojson.MultiLineString
import jp.co.lycorp.geojson.MultiPoint
import jp.co.lycorp.geojson.MultiPolygon
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Polygon

/**
 * Deserializer for all GeoJSON objects.
 */
class GeoJsonObjectDeserializer<T : GeoJsonObject> : JsonDeserializer<T>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val node: JsonNode = p.codec.readTree(p)
        val type = node.get("type")?.asText()
            ?: throw IllegalArgumentException("Missing 'type' property in GeoJSON")

        val mapper = (p.codec as ObjectMapper)

        val o = when (type) {
            "Point" -> mapper.treeToValue(node, Point::class.java)
            "MultiPoint" -> mapper.treeToValue(node, MultiPoint::class.java)
            "LineString" -> mapper.treeToValue(node, LineString::class.java)
            "MultiLineString" -> mapper.treeToValue(node, MultiLineString::class.java)
            "Polygon" -> mapper.treeToValue(node, Polygon::class.java)
            "MultiPolygon" -> mapper.treeToValue(node, MultiPolygon::class.java)
            "Feature" -> mapper.treeToValue(node, Feature::class.java)
            "FeatureCollection" -> mapper.treeToValue(node, FeatureCollection::class.java)
            else -> throw IllegalArgumentException("Unknown GeoJSON type: $type")
        }
        return o as T
    }
}
