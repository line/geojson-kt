package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PointSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize Point when valid Point is provided`() {
        val point = Point(Position(1.0, 1.0))
        val actual = mapper.writeValueAsString(point)
        val expected = """
            {
            "coordinates": [1.0, 1.0],
            "type": "Point"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize Point when valid Point with alt is provided`() {
        val point = Point(Position(1.0, 1.0, 44.0))
        val actual = mapper.writeValueAsString(point)
        val expected = """
            {
            "coordinates": [1.0, 1.0, 44.0],
            "type": "Point"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
