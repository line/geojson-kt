package jp.co.lycorp.geojson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.constant
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
import org.junit.jupiter.api.assertThrows

class LinearRingTest {
    @Test
    fun `should return true when point is on the linear ring's edge`() = runTest {
        val linearRing = LinearRing(
            listOf(
                Position(0.0, 0.0),
                Position(1.0, 0.0),
                Position(1.0, 1.0),
                Position(0.0, 1.0),
                Position(0.0, 0.0),
            ),
        )
        checkAll(
            Arb.double(0.0, 1.0),
            Arb.choice(Arb.constant(0.0), Arb.constant(1.0)),
        ) {
                lng: Double, lat: Double ->
            val actual = linearRing.isOnEdge(Position(lng, lat))
            assertTrue(actual)
        }
    }

    @Test
    fun `should return false when point is not on the linear ring's edge`() = runTest {
        val linearRing = LinearRing(
            listOf(
                Position(0.0, 0.0),
                Position(1.0, 0.0),
                Position(1.0, 1.0),
                Position(0.0, 1.0),
                Position(0.0, 0.0),
            ),
        )
        checkAll(
            Arb.geoLocation().filter {
                it.longitude != 0.0 && it.longitude != 1.0
            }.map {
                Position(it.longitude, it.latitude)
            },
        ) {
                geoLocation: Position ->
            val actual = linearRing.isOnEdge(Position(geoLocation.lng, geoLocation.lat))
            assertFalse(actual)
        }
    }

    @Test
    fun `should return true when point in linear ring is provided`() = runTest {
        val linearRing = LinearRing(
            listOf(
                Position(0.0, 0.0),
                Position(1.0, 0.0),
                Position(1.0, 1.0),
                Position(0.0, 1.0),
                Position(0.0, 0.0),
            ),
        )
        checkAll(
            Arb.double(0.0, 1.0).filter { it != 0.0 && it != 1.0 },
            Arb.double(0.0, 1.0).filter { it != 0.0 && it != 1.0 },
        ) {
                lng: Double, lat: Double ->
            val pointInLinearLing = Position(lng, lat)
            val actual1 = linearRing.isPointInLinearRing(pointInLinearLing)
            assertTrue(actual1)
            val actual2 = linearRing.isPointOutsideLinearRing(pointInLinearLing)
            assertFalse(actual2)
        }
    }

    @Test
    fun `should return false when point outside linear ring is provided`() = runTest {
        val linearRing = LinearRing(
            listOf(
                Position(0.0, 0.0),
                Position(1.0, 0.0),
                Position(1.0, 1.0),
                Position(0.0, 1.0),
                Position(0.0, 0.0),
            ),
        )
        checkAll(
            Arb.geoLocation().filter {
                it.longitude !in 0.0..1.0 && it.latitude !in 0.0..1.0
            }.map {
                Position(it.longitude, it.latitude)
            },
        ) {
                pointOutsideLinearLing: Position ->
            val actual1 = linearRing.isPointOutsideLinearRing(pointOutsideLinearLing)
            assertTrue(actual1)
            val actual2 = linearRing.isPointInLinearRing(pointOutsideLinearLing)
            assertFalse(actual2)
        }
    }

    @Test
    fun `should fail when empty list is passed`() = runTest {
        val expected = "LinearRing cannot be empty"
        val err = assertThrows<IllegalArgumentException> { LinearRing(emptyList()) }
        val actual = err.message
        assertEquals(expected, actual)
    }

    @Test
    fun `calculateCentroid should throw error when area is zero`() {
        val linearRing = LinearRing(
            listOf(
                Position(1.0, 1.0),
                Position(1.0, 1.0),
                Position(1.0, 1.0),
                Position(1.0, 1.0),
                Position(1.0, 1.0),
            ),
        )

        val exception = shouldThrow<IllegalArgumentException> {
            linearRing.calculateCentroid()
        }

        exception.message.shouldContain("The area of the polygon is zero.")
    }
}
