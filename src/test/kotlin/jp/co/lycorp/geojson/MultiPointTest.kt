package jp.co.lycorp.geojson

import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.geoLocation
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MultiPointTest {
    @Test
    fun `should split MultiPoint into Point`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
        checkAll(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) {
                position1: Position,
                position2: Position,
                position3: Position,
            ->
            val multiPoint = MultiPoint(
                listOf(
                    position1,
                    position2,
                    position3,
                ),
            )
            val actual = multiPoint.split()
            val expected = listOf(Point(position1), Point(position2), Point(position3))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should be able to add to MultiPoint when a valid Point is provided`() = runTest {
        val multiPoint = MultiPoint(
            listOf(Position(1.0, 1.0), Position(1.0, 2.0)),
        )
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
        checkAll(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) {
                position1: Position,
                position2: Position,
            ->
            val addedPoint1 = Point(position1)
            val addedPoint2 = Point(position2)
            val actual = multiPoint.added(addedPoint1, addedPoint2)
            val expected = MultiPoint(
                listOf(
                    Position(1.0, 1.0),
                    Position(1.0, 2.0),
                    position1,
                    position2,
                ),
                bbox = BBox(
                    minOf(1.0, position1.lng, position2.lng),
                    minOf(1.0, position1.lat, position2.lat),
                    maxOf(1.0, position1.lng, position2.lng),
                    maxOf(2.0, position1.lat, position2.lat),
                ),
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should create a MultiPoint instance when variable length arguments are provided`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
        checkAll(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) {
                position1: Position,
                position2: Position,
                position3: Position,
            ->
            val point1 = Point(position1)
            val point2 = Point(position2)
            val point3 = Point(position3)
            val actual = MultiPoint(point1, point2, point3)
            val expected = MultiPoint(
                listOf(position1, position2, position3),
                BBox(
                    minOf(position1.lng, position2.lng, position3.lng),
                    minOf(position1.lat, position2.lat, position3.lat),
                    maxOf(position1.lng, position2.lng, position3.lng),
                    maxOf(position1.lat, position2.lat, position3.lat),
                ),
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should derive from MultiPoint coordinates`() = runTest {
        val positionArb: Arb<Position> = Arb.geoLocation().map {
            Position(it.longitude, it.latitude)
        }
        checkAll(
            Arb.choice(positionArb),
            Arb.choice(positionArb),
            Arb.choice(positionArb),
        ) {
                position1: Position,
                position2: Position,
                position3: Position,
            ->
            val point1 = Point(position1)
            val point2 = Point(position2)
            val point3 = Point(position3)
            val multiPoint = MultiPoint(point1, point2, point3)
            val actual = multiPoint.calculateBBox()
            val expected =
                BBox(
                    minOf(position1.lng, position2.lng, position3.lng),
                    minOf(position1.lat, position2.lat, position3.lat),
                    maxOf(position1.lng, position2.lng, position3.lng),
                    maxOf(position1.lat, position2.lat, position3.lat),
                )
            assertEquals(expected, actual)
        }
    }
}
