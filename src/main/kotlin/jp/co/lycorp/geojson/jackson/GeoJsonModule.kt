package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.FeatureId
import jp.co.lycorp.geojson.Position

/**
 * Jackson module to install all serializers and deserializers for GeoJSON.
 */
class GeoJsonModule : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        val deserializers = SimpleDeserializers()
        deserializers.addDeserializer(BBox::class.java, BBoxDeserializer())
        deserializers.addDeserializer(FeatureId::class.java, FeatureIdDeserializer())
        deserializers.addDeserializer(Position::class.java, PositionDeserializer())
        context.addDeserializers(deserializers)

        val serializers = SimpleSerializers()
        serializers.addSerializer(BBox::class.java, BBoxSerializer())
        serializers.addSerializer(FeatureId::class.java, FeatureIdSerializer())
        serializers.addSerializer(Position::class.java, PositionSerializer())
        context.addSerializers(serializers)
    }
}
