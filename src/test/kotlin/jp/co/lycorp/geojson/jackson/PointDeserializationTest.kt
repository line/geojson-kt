package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PointDeserializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should deserialize to Point when valid GeoJSON is provided`() {
        val geoJson = """
            {
            "coordinates": [1.0, 1.0],
            "type": "Point"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, Point::class.java)
        val expected = Point(Position(1.0, 1.0))
        assertTrue(actual is Point)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to Point when valid GeoJSON with alt is provided`() {
        val geoJson = """
            {
            "coordinates": [1.0, 1.0, 44.0],
            "type": "Point"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, Point::class.java)
        val expected = Point(Position(1.0, 1.0, 44.0))
        assertTrue(actual is Point)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to Point when valid GeoJSON with bbox is provided`() {
        val point = Point(Position(1.0, 1.0), bbox = BBox(0.0, 0.0, 1.0, 1.0))
        val actual = mapper.writeValueAsString(point)
        val expected = """
            {
            "coordinates": [1.0, 1.0],
            "bbox": [0.0, 0.0, 1.0, 1.0],
            "type": "Point"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
