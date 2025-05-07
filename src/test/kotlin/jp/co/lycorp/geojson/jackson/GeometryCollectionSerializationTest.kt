package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.GeometryCollection
import jp.co.lycorp.geojson.LineString
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GeometryCollectionSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize GeometryCollection when valid GeometryCollection is provided`() {
        val point = Point(Position(10.0, 0.0))
        val lineString = LineString(
            listOf(
                Position(1.0, 0.0),
                Position(2.0, 1.0),
            ),
        )
        val geometryCollection = GeometryCollection(listOf(point, lineString))
        val actual = mapper.writeValueAsString(geometryCollection)
        val expected = """
            {
              "geometries": [
                {
                  "coordinates": [10.0, 0.0],
                  "type": "Point"
                },
                {
                  "coordinates": [
                    [1.0, 0.0],
                    [2.0, 1.0]
                  ],
                  "type": "LineString"
                }
              ],
              "type": "GeometryCollection"
            }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
