package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.LineString
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LineStringDeserializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should deserialize to LineString when valid GeoJSON is provided`() {
        val geoJson = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "type": "LineString"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, LineString::class.java)
        val expected = LineString(listOf(Position(1.0, 1.0), Position(1.0, 2.0)))
        assertTrue(actual is LineString)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to LineString when valid GeoJSON with alt is provided`() {
        val geoJson = """
            {
            "coordinates": [[1.0, 1.0, 0.0], [1.0, 2.0, 1.0]],
            "type": "LineString"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, LineString::class.java)
        val expected = LineString(listOf(Position(1.0, 1.0, 0.0), Position(1.0, 2.0, 1.0)))
        assertTrue(actual is LineString)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to LineString when valid GeoJSON with bbox is provided`() {
        val geoJson = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "bbox": [0.0, 0.0, 1.0, 1.0],
            "type": "LineString"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, LineString::class.java)
        val expected = LineString(
            listOf(Position(1.0, 1.0), Position(1.0, 2.0)),
            bbox = BBox(0.0, 0.0, 1.0, 1.0),
        )
        assertTrue(actual is LineString)
        assertEquals(expected, actual)
    }
}
