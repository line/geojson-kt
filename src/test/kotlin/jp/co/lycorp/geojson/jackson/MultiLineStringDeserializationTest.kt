package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.MultiLineString
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MultiLineStringDeserializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should deserialize to MultiLineString when valid GeoJSON is provided`() {
        val geoJson = """
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
        val actual = mapper.readValue(geoJson, MultiLineString::class.java)
        val lineString1 = listOf(
            Position(10.0, 0.0),
            Position(1.0, 1.0),
        )
        val lineString2 = listOf(
            Position(2.0, 2.0),
            Position(3.0, 3.0),
        )
        val expected = MultiLineString(listOf(lineString1, lineString2))
        assertTrue(actual is MultiLineString)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to MultiLineString when valid GeoJSON with bbox is provided`() {
        val geoJson = """
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
        val actual = mapper.readValue(geoJson, MultiLineString::class.java)
        val lineString1 = listOf(
            Position(10.0, 0.0),
            Position(1.0, 1.0),
        )
        val lineString2 = listOf(
            Position(2.0, 2.0),
            Position(3.0, 3.0),
        )
        val expected = MultiLineString(
            listOf(lineString1, lineString2),
            bbox = BBox(10.0, 0.0, 3.0, 3.0),
        )
        assertTrue(actual is MultiLineString)
        assertEquals(expected, actual)
    }
}
