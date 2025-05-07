package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.Polygon
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PolygonSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize Polygon when valid Polygon without holes is provided`() {
        val ring = listOf(
            Position(10.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(10.0, 1.0),
            Position(10.0, 0.0),
        )
        val polygon = Polygon(listOf(ring))
        val actual = mapper.writeValueAsString(polygon)
        val expected = """
            {
              "coordinates": [
                [
                  [10.0, 0.0],
                  [1.0, 0.0],
                  [1.0, 1.0],
                  [10.0, 1.0],
                  [10.0, 0.0]
                ]
              ],
             "type": "Polygon"
            }     
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize Polygon when valid Polygon with holes is provided`() {
        val ring = listOf(
            Position(0.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(0.0, 1.0),
            Position(0.0, 0.0),
        )
        val hole = listOf(
            Position(0.8, 0.8),
            Position(0.8, 0.2),
            Position(0.2, 0.2),
            Position(0.2, 0.8),
            Position(0.8, 0.8),
        )
        val polygon = Polygon(listOf(ring, hole))
        val actual = mapper.writeValueAsString(polygon)
        val expected = """
            {
              "coordinates": [
                [
                  [0.0, 0.0],
                  [1.0, 0.0],
                  [1.0, 1.0],
                  [0.0, 1.0],
                  [0.0, 0.0]
                ],
                [
                  [0.8, 0.8],
                  [0.8, 0.2],
                  [0.2, 0.2],
                  [0.2, 0.8],
                  [0.8, 0.8]
                ]
              ],
              "type": "Polygon"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize Polygon when valid Polygon with BBox is provided`() {
        val ring = listOf(
            Position(0.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(0.0, 1.0),
            Position(0.0, 0.0),
        )
        val polygon = Polygon(listOf(ring), bbox = BBox(0.0, 0.0, 1.0, 1.0))
        val actual = mapper.writeValueAsString(polygon)
        val expected = """
            {
              "coordinates": [
                [
                  [0.0, 0.0],
                  [1.0, 0.0],
                  [1.0, 1.0],
                  [0.0, 1.0],
                  [0.0, 0.0]
                ]
              ],
             "bbox":[0.0, 0.0, 1.0, 1.0],
             "type": "Polygon"
            }     
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
