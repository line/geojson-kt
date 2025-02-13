package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import jp.co.lycorp.geojson.FeatureId
import jp.co.lycorp.geojson.NumberFeatureId
import jp.co.lycorp.geojson.StringFeatureId

/**
 * FeatureIdSerializer class
 *
 * Custom serializer for feature id
 */
internal class FeatureIdSerializer : StdSerializer<FeatureId>(FeatureId::class.java) {
    override fun serialize(value: FeatureId, gen: JsonGenerator, serializer: SerializerProvider) {
        when (value) {
            is StringFeatureId -> gen.writeString(value.value)
            is NumberFeatureId -> gen.writeNumber(value.value.toString())
        }
    }
}
