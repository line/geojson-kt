package jp.co.lycorp.geojson

import io.kotest.common.DelicateKotest
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.distinct
import io.kotest.property.arbitrary.geoLocation
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FeatureCollectionTest {
    @Test
    @OptIn(DelicateKotest::class)
    fun `should derive from FeatureCollection coordinates`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }.distinct()

        val lineStringArb: Arb<List<Position>> = Arb.bind(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) { first, second, third -> listOf(first, second, third) }

        checkAll(
            lineStringArb,
            lineStringArb,
            lineStringArb,
        ) {
                firstLineStringCoords: List<Position>,
                secondLineStringCoords: List<Position>,
                thirdLineStringCoords: List<Position>,
            ->
            val lineString = LineString(firstLineStringCoords)
            val multiLineString = MultiLineString(listOf(secondLineStringCoords, thirdLineStringCoords))
            val feature1 = Feature(lineString)
            val feature2 = Feature(multiLineString)
            val featureCollection = FeatureCollection(listOf(feature1, feature2))
            val actual = featureCollection.calculateBBox()
            val expected = BBox(
                minOf(
                    firstLineStringCoords.minOf { it.lng },
                    secondLineStringCoords.minOf { it.lng },
                    thirdLineStringCoords.minOf { it.lng },
                ),
                minOf(
                    firstLineStringCoords.minOf { it.lat },
                    secondLineStringCoords.minOf { it.lat },
                    thirdLineStringCoords.minOf { it.lat },
                ),
                maxOf(
                    firstLineStringCoords.maxOf { it.lng },
                    secondLineStringCoords.maxOf { it.lng },
                    thirdLineStringCoords.maxOf { it.lng },
                ),
                maxOf(
                    firstLineStringCoords.maxOf { it.lat },
                    secondLineStringCoords.maxOf { it.lat },
                    thirdLineStringCoords.maxOf { it.lat },
                ),
            )
            assertEquals(expected, actual)
        }
    }
}
