package jp.co.lycorp.geojson

import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.GeoLocation
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.geoLocation
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun `should return true when a point in the polygon is passed`() = runTest {
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
            assertTrue(inPoint.isIn(polygon))
        }
    }

    @Test
    fun `should return false when a point on the polygon's edge is passed`() = runTest {
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
            val actual = onEdgePoint.isIn(polygon, allowOnEdge = false)
            assertFalse(actual)
        }
    }

    @Test
    fun `should return true when a point in the MultiPolygon is passed`() = runTest {
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
            assertTrue(inPoint.isIn(multiPolygon))
        }
    }

    @Test
    fun `bounding box should be equivalent to the point`() {
        val point = Point(Position(0.0, 1.1, 2.2))
        val expected = BBox(
            point.coordinates.lng,
            point.coordinates.lat,
            point.coordinates.lng,
            point.coordinates.lat,
            point.coordinates.alt,
            point.coordinates.alt,
        )

        point.calculateBBox().shouldBe(expected)
    }

    @Test
    fun `should return true when point is on an edge`() = runTest {
        val edge = Edge(
            Position(10.0, 10.0),
            Position(0.0, 0.0),
        )
        checkAll(
            Arb.double(0.0, 10.0),
        ) { double: Double ->
            Point(Position(double, double)).isOn(edge)
        }
    }

    @Test
    fun `should return false when point is not on an edge`() = runTest {
        val edge = Edge(
            Position(10.0, 10.0),
            Position(0.0, 0.0),
        )
        checkAll(
            Arb.geoLocation(),
        ) {
                geoLocation: GeoLocation ->
            if (geoLocation.longitude != geoLocation.latitude) {
                Point(Position(geoLocation.longitude, geoLocation.latitude)).isOn(edge).shouldBe(false)
            }
        }
    }
}
