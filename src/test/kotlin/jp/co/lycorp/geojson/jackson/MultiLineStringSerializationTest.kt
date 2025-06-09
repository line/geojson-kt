package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.MultiLineString
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MultiLineStringSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize MultiLineString when valid GeoJSON is provided`() {
        val lineString1 = listOf(
            Position(10.0, 0.0),
            Position(1.0, 1.0),
        )
        val lineString2 = listOf(
            Position(2.0, 2.0),
            Position(3.0, 3.0),
        )
        val multiLineString = MultiLineString(listOf(lineString1, lineString2))
        val actual = mapper.writeValueAsString(multiLineString)
        val expected = """
             {
                 "coordinates": [
                     [
                         [10.0, 0.0],
                         [1.0, 1.0]
                     ],
                     [
                         [2.0, 2.0],
                         [3.0, 3.0]
                     ]
                 ],
                 "type": "MultiLineString"
             }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize MultiLineString when valid MultiLineString with BBox is provided`() {
        val lineString1 = listOf(
            Position(10.0, 0.0),
            Position(1.0, 1.0),
        )
        val lineString2 = listOf(
            Position(2.0, 2.0),
            Position(3.0, 3.0),
        )
        val multiLineString = MultiLineString(
            listOf(lineString1, lineString2),
            bbox = BBox(10.0, 0.0, 3.0, 3.0),
        )
        val actual = mapper.writeValueAsString(multiLineString)
        val expected = """
             {
                 "coordinates": [
                     [
                         [10.0, 0.0],
                         [1.0, 1.0]
                     ],
                     [
                         [2.0, 2.0],
                         [3.0, 3.0]
                     ]
                 ],
                 "bbox":[10.0, 0.0, 3.0, 3.0],
                 "type": "MultiLineString"
             }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
