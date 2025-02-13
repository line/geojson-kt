package jp.co.lycorp.geojson

import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.GeoLocation
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.geoLocation
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EdgeTest {
    @Test
    fun `should return true when point is on this edge`() = runTest {
        val edge = Edge(
            Position(10.0, 10.0),
            Position(0.0, 0.0),
        )
        checkAll(
            Arb.double(0.0, 10.0),
        ) {
                double: Double ->
            val actual = edge.isCollinear(Position(double, double))
            assertTrue(actual)
        }
    }

    @Test
    fun `should return false when point is not on this edge`() = runTest {
        val edge = Edge(
            Position(10.0, 10.0),
            Position(0.0, 0.0),
        )
        checkAll(
            Arb.geoLocation(),
        ) {
                geoLocation: GeoLocation ->
            if (geoLocation.longitude != geoLocation.latitude) {
                val actual = edge.isCollinear(Position(geoLocation.longitude, geoLocation.latitude))
                assertFalse(actual)
            }
        }
    }

    @Test
    fun `should return true when collinear edges touch`() {
        val edge = Edge(
            Position(5.0, 0.0),
            Position(5.0, 1.0),
        )

        val otherEdge = Edge(
            Position(5.0, 1.0),
            Position(5.0, 2.0),
        )

        edge.isIntersected(otherEdge).shouldBe(true)
    }

    @Test
    fun `should return false when collinear edges do not touch`() = runTest {
        val edge = Edge(
            Position(5.0, 0.0),
            Position(5.0, 3.0),
        )

        val otherEdges = Exhaustive.collection(
            listOf(
                Edge(
                    Position(5.0, 4.0),
                    Position(5.0, 5.0),
                ),
                Edge(
                    Position(5.0, 5.0),
                    Position(5.0, 4.0),
                ),
            ),
        )

        checkAll(otherEdges) {
            edge.isIntersected(it).shouldBe(false)
        }
    }
}
