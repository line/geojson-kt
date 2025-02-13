package jp.co.lycorp.geojson

import io.kotest.common.DelicateKotest
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.distinct
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.geoLocation
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MultiPolygonTest {
    @Test
    fun `contains should return true when a point in the MultiPolygon is passed`() = runTest {
        val multiPolygon = MultiPolygon(
            Polygon(
                listOf(
                    listOf(
                        Position(0.0, 0.0),
                        Position(1.0, 0.0),
                        Position(1.0, 1.0),
                        Position(0.0, 1.0),
                        Position(0.0, 0.0),
                    ),
                    listOf(
                        Position(0.8, 0.8),
                        Position(0.8, 0.2),
                        Position(0.2, 0.2),
                        Position(0.2, 0.8),
                        Position(0.8, 0.8),
                    ),
                ),
            ),
            Polygon(
                listOf(
                    listOf(
                        Position(5.0, 5.0),
                        Position(6.0, 5.0),
                        Position(6.0, 6.0),
                        Position(5.0, 6.0),
                        Position(5.0, 5.0),
                    ),
                ),
            ),
        )

        checkAll(
            Arb.double(0.0, 1.0).filter { it !in 2.0..8.0 },
            Arb.double(0.0, 1.0).filter { it !in 0.2..8.0 },
        ) {
                lng: Double, lat: Double ->
            val inPoint = Point(Position(lng, lat))
            assertTrue(multiPolygon.contains(inPoint))
        }
    }

    @Test
    fun `contains should return true when a point on one of the MultiPolygon's edges is passed`() = runTest {
        val multiPolygon = MultiPolygon(
            Polygon(
                listOf(
                    listOf(
                        Position(0.0, 0.0),
                        Position(1.0, 0.0),
                        Position(1.0, 1.0),
                        Position(0.0, 1.0),
                        Position(0.0, 0.0),
                    ),
                    listOf(
                        Position(0.8, 0.8),
                        Position(0.8, 0.2),
                        Position(0.2, 0.2),
                        Position(0.2, 0.8),
                        Position(0.8, 0.8),
                    ),
                ),
            ),
            Polygon(
                listOf(
                    listOf(
                        Position(5.0, 5.0),
                        Position(6.0, 5.0),
                        Position(6.0, 6.0),
                        Position(5.0, 6.0),
                        Position(5.0, 5.0),
                    ),
                ),
            ),
        )

        checkAll(
            Arb.double(0.0, 1.0),
            Arb.choice(Arb.constant(0.0), Arb.constant(1.0)),
        ) {
                lng: Double, lat: Double ->
            val inPoint = Point(Position(lng, lat))
            assertTrue(multiPolygon.contains(inPoint))
        }
    }

    @OptIn(DelicateKotest::class)
    @Test
    fun `should split MultiPolygon into Polygon`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }.distinct()
        val polygonArb: Arb<List<Position>> = Arb.bind(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) { first, second, third, fourth -> listOf(first, second, third, fourth, first) }
        checkAll(
            polygonArb,
            polygonArb,
        ) {
                firstLinearRing: List<Position>,
                secondLinearRing: List<Position>,
            ->
            val multiPolygon = MultiPolygon(listOf(listOf(firstLinearRing), listOf(secondLinearRing)))
            val actual = multiPolygon.split()
            val expectedPolygon1 = Polygon(
                listOf(firstLinearRing),
                BBox(
                    firstLinearRing.minOf { it.lng },
                    firstLinearRing.minOf { it.lat },
                    firstLinearRing.maxOf { it.lng },
                    firstLinearRing.maxOf { it.lat },
                ),
            )
            val expectedPolygon2 = Polygon(
                listOf(secondLinearRing),
                BBox(
                    secondLinearRing.minOf { it.lng },
                    secondLinearRing.minOf { it.lat },
                    secondLinearRing.maxOf { it.lng },
                    secondLinearRing.maxOf { it.lat },
                ),
            )
            val expected = listOf(expectedPolygon1, expectedPolygon2)
            assertEquals(expected, actual)
        }
    }

    @OptIn(DelicateKotest::class)
    @Test
    fun `should be able to add to MultiPolygon when a valid Polygon is provided`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }.distinct()
        val polygonArb: Arb<List<Position>> = Arb.bind(
            positionArb,
            positionArb,
            positionArb,
            positionArb,
        ) { first, second, third, fourth -> listOf(first, second, third, fourth, first) }
        checkAll(
            polygonArb,
            polygonArb,
            polygonArb,
        ) {
                firstLinearRing: List<Position>,
                secondLinearRing: List<Position>,
                thirdLinearRing: List<Position>,
            ->
            val multiPolygon = MultiPolygon(listOf(listOf(firstLinearRing), listOf(secondLinearRing)))
            val addedPolygon = Polygon(listOf(thirdLinearRing))
            val actual = multiPolygon.added(addedPolygon)
            val expected = MultiPolygon(
                listOf(listOf(firstLinearRing), listOf(secondLinearRing), listOf(thirdLinearRing)),
                BBox(
                    minOf(
                        firstLinearRing.minOf { it.lng },
                        secondLinearRing.minOf { it.lng },
                        thirdLinearRing.minOf { it.lng },
                    ),
                    minOf(
                        firstLinearRing.minOf { it.lat },
                        secondLinearRing.minOf { it.lat },
                        thirdLinearRing.minOf { it.lat },
                    ),
                    maxOf(
                        firstLinearRing.maxOf { it.lng },
                        secondLinearRing.maxOf { it.lng },
                        thirdLinearRing.maxOf { it.lng },
                    ),
                    maxOf(
                        firstLinearRing.maxOf { it.lat },
                        secondLinearRing.maxOf { it.lat },
                        thirdLinearRing.maxOf { it.lat },
                    ),
                ),
            )
            assertEquals(expected, actual)
        }
    }

    @OptIn(DelicateKotest::class)
    @Test
    fun `should create a MultiPolygon instance when variable length arguments are provided`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }.distinct()
        val polygonArb: Arb<List<Position>> = Arb.bind(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) { first, second, third, fourth -> listOf(first, second, third, fourth, first) }
        checkAll(
            polygonArb,
            polygonArb,
        ) {
                firstLinearRing: List<Position>,
                secondLinearRing: List<Position>,
            ->
            val firstPolygon = Polygon(listOf(firstLinearRing))
            val secondPolygon = Polygon(listOf(secondLinearRing))
            val actual = MultiPolygon(firstPolygon, secondPolygon)
            val expected = MultiPolygon(
                listOf(listOf(firstLinearRing), listOf(secondLinearRing)),
                BBox(
                    minOf(
                        firstLinearRing.minOf { it.lng },
                        secondLinearRing.minOf { it.lng },
                    ),
                    minOf(
                        firstLinearRing.minOf { it.lat },
                        secondLinearRing.minOf { it.lat },
                    ),
                    maxOf(
                        firstLinearRing.maxOf { it.lng },
                        secondLinearRing.maxOf { it.lng },
                    ),
                    maxOf(
                        firstLinearRing.maxOf { it.lat },
                        secondLinearRing.maxOf { it.lat },
                    ),
                ),
            )
            assertEquals(expected, actual)
        }
    }

    @OptIn(DelicateKotest::class)
    @Test
    fun `should derive from MultiPolygon coordinates`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }.distinct()
        val polygonArb: Arb<List<Position>> = Arb.bind(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) { first, second, third, fourth -> listOf(first, second, third, fourth, first) }
        checkAll(
            polygonArb,
            polygonArb,
        ) {
                firstLinearRing: List<Position>,
                secondLinearRing: List<Position>,
            ->
            val firstPolygon = Polygon(listOf(firstLinearRing))
            val secondPolygon = Polygon(listOf(secondLinearRing))
            val multiPolygon = MultiPolygon(firstPolygon, secondPolygon)
            val actual = multiPolygon.calculateBBox()
            val expected =
                BBox(
                    minOf(
                        firstLinearRing.minOf { it.lng },
                        secondLinearRing.minOf { it.lng },
                    ),
                    minOf(
                        firstLinearRing.minOf { it.lat },
                        secondLinearRing.minOf { it.lat },
                    ),
                    maxOf(
                        firstLinearRing.maxOf { it.lng },
                        secondLinearRing.maxOf { it.lng },
                    ),
                    maxOf(
                        firstLinearRing.maxOf { it.lat },
                        secondLinearRing.maxOf { it.lat },
                    ),
                )
            assertEquals(expected, actual)
        }
    }
}
