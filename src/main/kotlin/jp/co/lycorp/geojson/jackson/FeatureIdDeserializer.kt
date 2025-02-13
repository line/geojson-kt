package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import jp.co.lycorp.geojson.FeatureId

/**
 * FeatureIdDeserializer class
 *
 * Custom deserializer for feature id.
 */
internal class FeatureIdDeserializer : StdDeserializer<Any?>(FeatureId::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): FeatureId {
        return when {
            p.currentToken.isNumeric -> FeatureId.of(p.numberValue)
            !p.currentToken.isBoolean && p.currentToken.isScalarValue -> FeatureId.of(p.text)
            else -> throw IllegalArgumentException("Only String or Number is allowed in the id field")
        }
    }
}
