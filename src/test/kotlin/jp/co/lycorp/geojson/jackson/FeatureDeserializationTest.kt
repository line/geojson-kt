package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.Feature
import jp.co.lycorp.geojson.FeatureId
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Polygon
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class FeatureDeserializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should deserialize to Feature when valid GeoJSON with id of a string is provided`() {
        val geoJson = """{
          "type": "Feature",
          "id": "21",
          "geometry": {
            "type": "Point",
            "coordinates": [10.0, 0.0]
          }
        }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, Feature::class.java)
        val expected = Feature(id = FeatureId.of("21"), geometry = Point(Position(10.0, 0.0)))
        assertEquals(expected, actual)
        assertEquals(FeatureId.of("21"), actual.id)
        assertTrue(actual.properties == null)
        assertTrue(actual.geometry is Point)
    }

    @Test
    fun `should deserialize to Feature when valid GeoJSON with numeric id is provided`() {
        val geoJson = """{
          "type": "Feature",
          "id": 21,
          "geometry": {
            "type": "Point",
            "coordinates": [10.0, 0.0]
          }
        }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, Feature::class.java)
        val expected = Feature(id = FeatureId.of(21), geometry = Point(Position(10.0, 0.0)))
        assertEquals(expected, actual)
        assertEquals(FeatureId.of(21), actual.id)
        assertTrue(actual.properties == null)
        assertTrue(actual.geometry is Point)
    }

    @Test
    fun `should deserialize Feature when valid Feature with BBox is provided`() {
        val geoJson = """{
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
        val actual = mapper.readValue(geoJson, Feature::class.java)
        val expected = Feature(
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
        assertEquals(expected, actual)
    }

    @Test
    fun `should throw error when id type is invalid`() = runTest {
        val testCases = Exhaustive.collection(
            listOf(
                """
                {
                  "type": "Feature",
                  "id": {},
                  "geometry": {
                    "type": "Point",
                    "coordinates": [10.0, 0.0]
                  }
                }
            """.toCompactedJson(),
                """
                {
                  "type": "Feature",
                  "id": []
                  "geometry": {
                    "type": "Point",
                    "coordinates": [10.0, 0.0]
                  }
                }
            """.toCompactedJson(),
                """
                {
                  "type": "Feature",
                  "id": true,
                  "geometry": {
                    "type": "Point",
                    "coordinates": [10.0, 0.0]
                  }
                }
            """.toCompactedJson(),
            ),
        )

        checkAll(testCases) {
            val exception = shouldThrow<IOException> {
                mapper.readValue(it, Feature::class.java)
            }
            exception.message.shouldContain("Only String or Number is allowed in the id field")
        }
    }
}
