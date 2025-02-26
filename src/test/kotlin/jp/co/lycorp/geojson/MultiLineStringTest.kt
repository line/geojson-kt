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

class MultiLineStringTest {
    @Test
    fun `should split MultiLineString into LineString`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
        val lineStringArb: Arb<List<Position>> = Arb.bind(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) { first, second, third -> listOf(first, second, third) }
        checkAll(
            lineStringArb,
            lineStringArb,
        ) {
                firstLineString: List<Position>,
                secondLineString: List<Position>,
            ->
            val multiLineString = MultiLineString(listOf(firstLineString, secondLineString))
            val actual = multiLineString.split()
            val expectedList1 = LineString(
                firstLineString,
                BBox(
                    firstLineString.minOf { it.lng },
                    firstLineString.minOf { it.lat },
                    firstLineString.maxOf { it.lng },
                    firstLineString.maxOf { it.lat },
                ),
            )
            val expectedList2 = LineString(
                secondLineString,
                BBox(
                    secondLineString.minOf { it.lng },
                    secondLineString.minOf { it.lat },
                    secondLineString.maxOf { it.lng },
                    secondLineString.maxOf { it.lat },
                ),
            )
            val expected = listOf(expectedList1, expectedList2)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should be able to add a LineString when a valid LineString is provided`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
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
                firstLineString: List<Position>,
                secondLineString: List<Position>,
                thirdLineString: List<Position>,
            ->
            val multiLineString = MultiLineString(listOf(firstLineString, secondLineString))
            val addedLineString = LineString(thirdLineString)
            val actual = multiLineString.added(addedLineString)
            val expected = MultiLineString(
                listOf(firstLineString, secondLineString, thirdLineString),
                BBox(
                    minOf(
                        firstLineString.minOf { it.lng },
                        secondLineString.minOf { it.lng },
                        thirdLineString.minOf { it.lng },
                    ),
                    minOf(
                        firstLineString.minOf { it.lat },
                        secondLineString.minOf { it.lat },
                        thirdLineString.minOf { it.lat },
                    ),
                    maxOf(
                        firstLineString.maxOf { it.lng },
                        secondLineString.maxOf { it.lng },
                        thirdLineString.maxOf { it.lng },
                    ),
                    maxOf(
                        firstLineString.maxOf { it.lat },
                        secondLineString.maxOf { it.lat },
                        thirdLineString.maxOf { it.lat },
                    ),
                ),
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should create a MultiLineString instance when variable length arguments are provided`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
        val lineStringArb: Arb<List<Position>> = Arb.bind(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) { first, second, third -> listOf(first, second, third) }
        checkAll(
            lineStringArb,
            lineStringArb,
        ) {
                firstLineStringCoordinates: List<Position>,
                secondLineStringCoordinates: List<Position>,
            ->
            val firstLineString = LineString(firstLineStringCoordinates)
            val secondLineString = LineString(secondLineStringCoordinates)
            val actual = MultiLineString(firstLineString, secondLineString)
            val expected = MultiLineString(
                listOf(firstLineStringCoordinates, secondLineStringCoordinates),
                BBox(
                    minOf(
                        firstLineStringCoordinates.minOf { it.lng },
                        secondLineStringCoordinates.minOf { it.lng },
                    ),
                    minOf(
                        firstLineStringCoordinates.minOf { it.lat },
                        secondLineStringCoordinates.minOf { it.lat },
                    ),
                    maxOf(
                        firstLineStringCoordinates.maxOf { it.lng },
                        secondLineStringCoordinates.maxOf { it.lng },
                    ),
                    maxOf(
                        firstLineStringCoordinates.maxOf { it.lat },
                        secondLineStringCoordinates.maxOf { it.lat },
                    ),
                ),
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    @OptIn(DelicateKotest::class)
    fun `should derive from MultiLineString coordinates`() = runTest {
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
        ) {
                firstLineStringCoordinates: List<Position>,
                secondLineStringCoordinates: List<Position>,
            ->
            val firstLineString = LineString(firstLineStringCoordinates)
            val secondLineString = LineString(secondLineStringCoordinates)
            val multiLineString = MultiLineString(firstLineString, secondLineString)
            val actual = multiLineString.calculateBBox()
            val expected = BBox(
                minOf(
                    firstLineStringCoordinates.minOf { it.lng },
                    secondLineStringCoordinates.minOf { it.lng },
                ),
                minOf(
                    firstLineStringCoordinates.minOf { it.lat },
                    secondLineStringCoordinates.minOf { it.lat },
                ),
                maxOf(
                    firstLineStringCoordinates.maxOf { it.lng },
                    secondLineStringCoordinates.maxOf { it.lng },
                ),
                maxOf(
                    firstLineStringCoordinates.maxOf { it.lat },
                    secondLineStringCoordinates.maxOf { it.lat },
                ),
            )
            assertEquals(expected, actual)
        }
    }
}
