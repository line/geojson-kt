package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.GeometryCollection
import jp.co.lycorp.geojson.LineString
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GeometryCollectionDeserializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should deserialize to GeometryCollection when valid GeoJSON is provided`() {
        val geoJson = """
            {
              "geometries": [
                {
                  "type": "Point",
                  "coordinates": [10.0, 0.0]
                },
                {
                  "type": "LineString",
                  "coordinates": [
                    [1.0, 0.0],
                    [2.0, 1.0]
                  ]
                }
              ],
              "type": "GeometryCollection"
            }
        """.toCompactedJson()
        val actual = mapper.readValue(geoJson, GeometryCollection::class.java)
        val point = Point(Position(10.0, 0.0))
        val lineString = LineString(
            listOf(
                Position(1.0, 0.0),
                Position(2.0, 1.0),
            ),
        )
        val expected = GeometryCollection(listOf(point, lineString))
        assertTrue(actual is GeometryCollection)
        assertEquals(expected, actual)
    }
}
