package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.MultiPoint
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MultiPointSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize to MultiPoint when valid MultiPoint is provided`() {
        val multiPoint = MultiPoint(listOf(Position(1.0, 1.0), Position(1.0, 2.0)))
        val actual = mapper.writeValueAsString(multiPoint)
        val expected = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "type": "MultiPoint"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize to MultiPoint when valid MultiPoint with alt is provided`() {
        val multiPoint = MultiPoint(
            listOf(
                Position(1.0, 1.0, 0.0),
                Position(1.0, 2.0, 1.0),
            ),
        )
        val actual = mapper.writeValueAsString(multiPoint)
        val expected = """
            {
            "coordinates": [[1.0, 1.0, 0.0], [1.0, 2.0, 1.0]],
            "type": "MultiPoint"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize to MultiPoint when valid MultiPoint with BBox is provided`() {
        val multiPoint = MultiPoint(
            listOf(Position(1.0, 1.0), Position(1.0, 2.0)),
            bbox = BBox(1.0, 1.0, 1.0, 2.0),
        )
        val actual = mapper.writeValueAsString(multiPoint)
        val expected = """
            {
            "coordinates": [[1.0, 1.0], [1.0, 2.0]],
            "bbox": [1.0, 1.0, 1.0, 2.0],
            "type": "MultiPoint"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
