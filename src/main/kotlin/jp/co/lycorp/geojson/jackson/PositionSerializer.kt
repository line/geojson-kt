package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import jp.co.lycorp.geojson.Position

internal class PositionSerializer : StdSerializer<Position>(Position::class.java) {
    override fun serialize(value: Position, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartArray()
        gen.writeNumber(value.lng)
        gen.writeNumber(value.lat)
        value.alt?.let { gen.writeNumber(it) }
        gen.writeEndArray()
    }
}
