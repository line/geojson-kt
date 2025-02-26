package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.Feature
import jp.co.lycorp.geojson.FeatureId
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Polygon
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FeatureSerializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should serialize Feature when valid Feature with id of a string is provided`() {
        val feature = Feature(id = FeatureId.of(1), geometry = Point(Position(10.0, 0.0)))
        val actual = mapper.writeValueAsString(feature)
        val expected = """{
          "geometry": {
            "coordinates": [10.0, 0.0],
            "type": "Point"
          },
          "id": 1,
          "type": "Feature"
        }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize Feature when valid Feature with BBox is provided`() {
        val feature = Feature(
            geometry = Polygon(
                listOf(
                    listOf(
                        Position(-10.0, -10.0),
                        Position(10.0, -10.0),
                        Position(10.0, 10.0),
                        Position(-10.0, -10.0),
                    ),
                ),
            ),
            bbox = BBox(-10.0, -10.0, 10.0, 10.0),
        )
        val actual = mapper.writeValueAsString(feature)
        val expected = """{
          "geometry": {
           "coordinates": [
               [
                   [-10.0, -10.0],
                   [10.0, -10.0],
                   [10.0, 10.0],
                   [-10.0, -10.0]
               ]
           ],
           "type": "Polygon"
          },
          "bbox": [-10.0, -10.0, 10.0, 10.0],
          "type": "Feature"
        }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
