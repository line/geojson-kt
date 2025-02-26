package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.LineString
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LineStringSerializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should serialize LineString when valid LineString is provided`() {
        val lineString = LineString(listOf(Position(1.0, 1.0), Position(1.0, 2.0)))
        val actual = mapper.writeValueAsString(lineString)
        val expected = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "type": "LineString"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize LineString when valid LineString with alt is provided`() {
        val lineString = LineString(listOf(Position(1.0, 1.0, 0.0), Position(1.0, 2.0, 1.0)))
        val actual = mapper.writeValueAsString(lineString)
        val expected = """
            {
            "coordinates": [[1.0, 1.0,0.0], [1.0, 2.0,1.0]],
            "type": "LineString"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize LineString when valid LineString with BBox is provided`() {
        val lineString = LineString(
            listOf(
                Position(1.0, 1.0),
                Position(1.0, 2.0),
            ),
            bbox = BBox(0.0, 0.0, 1.0, 1.0),
        )
        val actual = mapper.writeValueAsString(lineString)
        val expected = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "bbox": [0.0, 0.0, 1.0, 1.0],
            "type": "LineString"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
