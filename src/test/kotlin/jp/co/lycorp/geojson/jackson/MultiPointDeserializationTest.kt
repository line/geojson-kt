package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.MultiPoint
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MultiPointDeserializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should deserialize to MultiPoint when valid GeoJSON is provided`() {
        val geoJson = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "type": "MultiPoint"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, MultiPoint::class.java)
        val expected = MultiPoint(listOf(Position(1.0, 1.0), Position(1.0, 2.0)))
        assertTrue(actual is MultiPoint)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to MultiPoint when valid GeoJSON with alt is provided`() {
        val geoJson = """
            {
            "coordinates": [[1.0, 1.0, 0.0], [1.0, 2.0, 1.0]],
            "type": "MultiPoint"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, MultiPoint::class.java)
        val expected = MultiPoint(listOf(Position(1.0, 1.0, 0.0), Position(1.0, 2.0, 1.0)))
        assertTrue(actual is MultiPoint)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to MultiPoint when valid GeoJSON with bbox is provided`() {
        val geoJson = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "bbox": [1.0, 1.0, 1.0, 2.0],
            "type": "MultiPoint"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, MultiPoint::class.java)
        val expected = MultiPoint(
            listOf(Position(1.0, 1.0), Position(1.0, 2.0)),
            bbox = BBox(1.0, 1.0, 1.0, 2.0),
        )
        assertTrue(actual is MultiPoint)
        assertEquals(expected, actual)
    }
}
