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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.math.sqrt

class PolygonTest {
    @Test
    fun `contains should return true when a point in the polygon is passed`() = runTest {
        val ring = listOf(
            Position(0.0, 0.0),
            Position(1.0, 0.0),
            Position(1.0, 1.0),
            Position(0.0, 1.0),
            Position(0.0, 0.0),
        )
        val hole = listOf(
            Position(0.8, 0.8),
            Position(0.8, 0.2),
            Position(0.2, 0.2),
            Position(0.2, 0.8),
            Position(0.8, 0.8),
        )
        val polygon = Polygon(listOf(ring, hole))
        checkAll(
            Arb.double(0.0, 1.0).filter { it !in 2.0..8.0 },
            Arb.double(0.0, 1.0).filter { it !in 0.2..8.0 },
        ) {
                lng: Double, lat: Double ->
            val inPoint = Point(Position(lng, lat))
            assertTrue(polygon.contains(inPoint))
        }
    }

    @Test
    fun `contains should return false when allowEdge = false and point on the polygon's edge is passed`() =
        runTest {
            val ring = listOf(
                Position(0.0, 0.0),
                Position(1.0, 0.0),
                Position(1.0, 1.0),
                Position(0.0, 1.0),
                Position(0.0, 0.0),
            )
            val hole = listOf(
                Position(0.8, 0.8),
                Position(0.8, 0.2),
                Position(0.2, 0.2),
                Position(0.2, 0.8),
                Position(0.8, 0.8),
            )
            val polygon = Polygon(listOf(ring, hole))
            checkAll(
                Arb.double(0.0, 1.0),
                Arb.choice(Arb.constant(0.0), Arb.constant(1.0)),
            ) {
                    lng: Double, lat: Double ->
                val onEdgePoint = Point(Position(lng, lat))
                val actual = polygon.contains(onEdgePoint, allowOnEdge = false)
                assertFalse(actual)
            }
        }

    @Test
    fun `should be able to calculate centroid`() {
        val square = Polygon(
            listOf(
                listOf(
                    Position(1.0, 1.0),
                    Position(-1.0, 1.0),
                    Position(-1.0, -1.0),
                    Position(1.0, -1.0),
                    Position(1.0, 1.0),
                ),
            ),
        )
        val regularHexagon = Polygon(
            listOf(
                listOf(
                    Position(1.0, 0.0),
                    Position(0.5, sqrt(3.0) / 2),
                    Position(-0.5, sqrt(3.0) / 2),
                    Position(-1.0, 0.0),
                    Position(-0.5, -sqrt(3.0) / 2),
                    Position(0.5, -sqrt(3.0) / 2),
                    Position(1.0, 0.0),
                ),
            ),
        )

        val actual1 = square.calculateCentroid()
        val expected1 = Position(0.0, 0.0)
        assertEquals(expected1, actual1)
        val actual2 = regularHexagon.calculateCentroid()
        val expected2 = Position(0.0, 0.0)
        assertEquals(expected2.lng, actual2.lng, 10.0.pow(18.0))
        assertEquals(expected2.lat, actual2.lat, 10.0.pow(18.0))
    }

    @OptIn(DelicateKotest::class)
    @Test
    fun `should derive from Polygon coordinates`() = runTest {
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
        ) {
                linearRing: List<Position>,
            ->
            val polygon = Polygon(listOf(linearRing))
            val actual = polygon.calculateBBox()
            val expected =
                BBox(
                    minOf(linearRing.minOf { it.lng }),
                    minOf(linearRing.minOf { it.lat }),
                    maxOf(linearRing.maxOf { it.lng }),
                    maxOf(linearRing.maxOf { it.lat }),
                )
            assertEquals(expected, actual)
        }
    }
}
