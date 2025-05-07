package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.MultiPolygon
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MultiPolygonSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize MultiPolygon when valid MultiPolygon is provided`() {
        val polygon1 = listOf(
            Position(2.0, 2.0),
            Position(3.0, 2.0),
            Position(3.0, 3.0),
            Position(2.0, 3.0),
            Position(2.0, 2.0),
        )
        val polygon2Ring1 = listOf(
            Position(0.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(0.0, 1.0),
            Position(0.0, 0.0),
        )
        val polygon2Ring2 = listOf(
            Position(0.2, 0.2),
            Position(0.2, 0.8),
            Position(0.8, 0.8),
            Position(0.8, 0.2),
            Position(0.2, 0.2),
        )
        val multiPolygon = MultiPolygon(listOf(listOf(polygon1), listOf(polygon2Ring1, polygon2Ring2)))
        val actual = mapper.writeValueAsString(multiPolygon)
        val expected = """
            {
              "coordinates": [
                [
                  [
                    [2.0, 2.0],
                    [3.0, 2.0],
                    [3.0, 3.0],
                    [2.0, 3.0],
                    [2.0, 2.0]
                  ]
                ],
                [
                  [
                    [0.0, 0.0],
                    [1.0, 0.0],
                    [1.0, 1.0],
                    [0.0, 1.0],
                    [0.0, 0.0]
                  ],
                  [
                    [0.2, 0.2],
                    [0.2, 0.8],
                    [0.8, 0.8],
                    [0.8, 0.2],
                    [0.2, 0.2]
                  ]
                ]
              ],
              "type": "MultiPolygon"
            }  
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
