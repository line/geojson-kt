package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import jp.co.lycorp.geojson.BBox

internal class BBoxSerializer : StdSerializer<BBox>(
    BBox::class.java,
) {
    override fun serialize(value: BBox, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartArray()
        gen.writeNumber(value.minLng)
        gen.writeNumber(value.minLat)
        value.minAlt?.let { gen.writeNumber(it) }
        gen.writeNumber(value.maxLng)
        gen.writeNumber(value.maxLat)
        value.maxAlt?.let { gen.writeNumber(it) }
        gen.writeEndArray()
    }
}
