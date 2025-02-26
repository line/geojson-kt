package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.Polygon
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PolygonDeserializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should deserialize to Polygon when valid GeoJSON without holes is provided`() {
        val geoJson = """
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
             "type": "Polygon"
            }     
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, Polygon::class.java)
        val ring = listOf(
            Position(0.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(0.0, 1.0),
            Position(0.0, 0.0),
        )
        val expected = Polygon(listOf(ring))
        assertTrue(actual is Polygon)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to Polygon when valid GeoJSON with holes is provided`() {
        val geoJson = """
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
        val actual = mapper.readValue(geoJson, Polygon::class.java)
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
        val expected = Polygon(listOf(ring, hole))
        assertTrue(actual is Polygon)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to Polygon when valid GeoJSON with bbox is provided`() {
        val geoJson = """
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
        val actual = mapper.readValue(geoJson, Polygon::class.java)
        val ring = listOf(
            Position(0.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(0.0, 1.0),
            Position(0.0, 0.0),
        )
        val expected = Polygon(listOf(ring), bbox = BBox(0.0, 0.0, 1.0, 1.0))
        assertTrue(actual is Polygon)
        assertEquals(expected, actual)
    }
}
