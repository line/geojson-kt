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

class FeatureCollectionDeserializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should deserialize to FeatureCollection when valid GeoJSON with id is provided`() {
        val geoJson = """{
           "type": "FeatureCollection",
           "features": [{
               "type": "Feature",
               "geometry": {
                   "type": "Point",
                   "coordinates": [2.0, 0.5]
               },
               "properties": {
                   "prop0": "value0"
               }
           }, {
               "type": "Feature",
               "geometry": {
                   "type": "LineString",
                   "coordinates": [
                       [2.0, 0.0],
                       [3.0, 1.0],
                       [4.0, 0.0],
                       [5.0, 1.0]
                   ]
               },
               "properties": {
                   "prop0": "value0",
                   "prop1": 0.0
               }
           }]
       }
        """.toCompactedJson()

        val actual = mapper.readValue(geoJson, FeatureCollection::class.java)
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
        val expected = FeatureCollection(listOf(feature1, feature2))
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to FeatureCollection when valid GeoJSON with bbox is provided`() {
        val geoJson = """{
           "type": "FeatureCollection",
           "bbox":[0.0, 0.0, 5.0, 1.0],
           "features": [{
               "type": "Feature",
               "geometry": {
                   "type": "Point",
                   "coordinates": [2.0, 0.5]
                    },
               "properties": {
                   "prop0": "value0"
                    }
               },
               {
               "type": "Feature",
               "geometry": {
                   "type": "LineString",
                   "coordinates": [
                       [2.0, 0.0],
                       [3.0, 1.0],
                       [4.0, 0.0],
                       [5.0, 1.0]
                   ]},
               "properties": {
                   "prop0": "value0",
                   "prop1": 0.0
               }
           }]
       }
        """.toCompactedJson()

        val actual = mapper.readValue(geoJson, FeatureCollection::class.java)
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
        val expected = FeatureCollection(
            listOf(feature1, feature2),
            bbox = BBox(0.0, 0.0, 5.0, 1.0),
        )
        assertEquals(expected, actual)
    }
}
