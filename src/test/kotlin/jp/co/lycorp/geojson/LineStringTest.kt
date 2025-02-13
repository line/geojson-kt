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

class LineStringTest {
    @Test
    @OptIn(DelicateKotest::class)
    fun `should derive from LineString coordinates`() = runTest {
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
        ) {
                lineStringCoords: List<Position>,
            ->
            val lineString = LineString(lineStringCoords)
            val actual = lineString.calculateBBox()
            val expected = BBox(
                lineStringCoords.minOf { it.lng },
                lineStringCoords.minOf { it.lat },
                lineStringCoords.maxOf { it.lng },
                lineStringCoords.maxOf { it.lat },
            )
            assertEquals(expected, actual)
        }
    }
}
