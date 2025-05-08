package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.module.SimpleSerializers
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.FeatureId
import jp.co.lycorp.geojson.GeoJsonObject
import jp.co.lycorp.geojson.Geometry
import jp.co.lycorp.geojson.Position

/**
 * Jackson module to install all serializers and deserializers for GeoJSON.
 */
class GeoJsonModule : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)

        val deserializers = SimpleDeserializers()
        deserializers.addDeserializer(BBox::class.java, BBoxDeserializer())
        deserializers.addDeserializer(FeatureId::class.java, FeatureIdDeserializer())
        deserializers.addDeserializer(Position::class.java, PositionDeserializer())
        deserializers.addDeserializer(Geometry::class.java, GoeJsonObjectDeserializer())

        context.addDeserializers(deserializers)

        val serializers = SimpleSerializers()
        serializers.addSerializer(BBox::class.java, BBoxSerializer())
        serializers.addSerializer(FeatureId::class.java, FeatureIdSerializer())
        serializers.addSerializer(Position::class.java, PositionSerializer())
        context.addSerializers(serializers)

        context.insertAnnotationIntrospector(object : NopAnnotationIntrospector() {
            override fun findPropertyInclusion(a: Annotated?): JsonInclude.Value? {
                val rawType = a?.rawType ?: return null

                return if (GeoJsonObject::class.java.isAssignableFrom(rawType)) {
                    JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL)
                } else {
                    null
                }
            }
        })
    }
}
