package jp.co.lycorp.geojson.jackson

import jp.co.lycorp.geojson.BBox
import jp.co.lycorp.geojson.Feature
import jp.co.lycorp.geojson.FeatureCollection
import jp.co.lycorp.geojson.LineString
import jp.co.lycorp.geojson.Point
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.extensions.JsonUtils.toCompactedJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FeatureCollectionSerializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should serialize FeatureCollection when valid feature collection with id is provided`() {
        val feature1 = Feature(
            geometry = Point(Position(2.0, 0.5)),
            properties = mapOf("prop0" to "value0"),
        )
        val feature2 = Feature(
            geometry = LineString(
                listOf(
                    Position(2.0, 0.0),
                    Position(3.0, 1.0),
                    Position(4.0, 0.0),
                    Position(5.0, 1.0),
                ),
            ),
            properties = mapOf("prop0" to "value0", "prop1" to 0.0),
        )
        val featureCollection = FeatureCollection(listOf(feature1, feature2))
        val actual = mapper.writeValueAsString(featureCollection)
        val expected = """{
           "features": [{
               "geometry": {
                   "coordinates": [2.0, 0.5],
                   "type": "Point"
               },
               "properties": {
                   "prop0": "value0"
               },
                "type": "Feature"
           }, {
               "geometry": {
                   "coordinates": [
                       [2.0, 0.0],
                       [3.0, 1.0],
                       [4.0, 0.0],
                       [5.0, 1.0]
                   ],
                   "type": "LineString"             
               },
               "properties": {
                   "prop0": "value0",
                   "prop1": 0.0
               },
               "type": "Feature"
           }],
           "type": "FeatureCollection"
       }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize FeatureCollection when valid feature collection with BBox is provided`() {
        val feature1 = Feature(
            geometry = Point(Position(2.0, 0.5)),
            properties = mapOf("prop0" to "value0"),
        )
        val feature2 = Feature(
            geometry = LineString(
                listOf(
                    Position(2.0, 0.0),
                    Position(3.0, 1.0),
                    Position(4.0, 0.0),
                    Position(5.0, 1.0),
                ),
            ),
            properties = mapOf("prop0" to "value0", "prop1" to 0.0),
        )
        val featureCollection = FeatureCollection(
            listOf(feature1, feature2),
            bbox = BBox(0.0, 0.0, 5.0, 1.0),
        )
        val actual = mapper.writeValueAsString(featureCollection)
        val expected = """{
           "features": [{
               "geometry": {
                   "coordinates": [2.0, 0.5],
                   "type": "Point"
                    },
               "properties": {
                   "prop0": "value0"
                    },
               "type": "Feature"
               },
               {
               "geometry": {
                   "coordinates": [
                       [2.0, 0.0],
                       [3.0, 1.0],
                       [4.0, 0.0],
                       [5.0, 1.0]
                   ],
                   "type": "LineString"
                   },
               "properties": {
                   "prop0": "value0",
                   "prop1": 0.0
               },
               "type": "Feature"
           }],
           "bbox":[0.0, 0.0, 5.0, 1.0],
           "type": "FeatureCollection"
           }
        """.toCompactedJson()
        assertEquals(expected, actual)
    }
}
