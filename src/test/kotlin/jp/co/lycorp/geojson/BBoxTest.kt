package jp.co.lycorp.geojson

import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BBoxTest {
    @Test
    fun `should derive BBox when an array of 2D Positions is provided`() {
        val positionList = listOf(
            Position(-8.9, 1.0),
            Position(2.9, 0.0),
            Position(11.9, -43.0),
            Position(8.9, 1.0),
        )
        val actual = BBox.from(positionList)
        val expected = BBox(-8.9, -43.0, 11.9, 1.0)
        assertEquals(expected, actual)
    }

    @Test
    fun `should derive BBox when an array of 3D Positions is provided`() {
        val positionList = listOf(
            Position(-8.9, 1.0, -7.1),
            Position(2.9, 0.0, 0.0),
            Position(11.9, -43.0, 1.0),
            Position(8.9, 1.0, 7.2),
        )
        val actual = BBox.from(positionList)
        val expected = BBox(-8.9, -43.0, 11.9, 1.0, -7.1, 7.2)
        assertEquals(expected, actual)
    }

    @Test
    fun `should derive BBox when an array of 3D Positions (with alt partially set) is provided`() {
        val positionList = listOf(
            Position(-8.9, 1.0, null),
            Position(2.9, 0.0, 7.2),
            Position(11.9, -43.0, null),
            Position(8.9, 1.0, -7.1),
        )
        val actual = BBox.from(positionList)
        val expected = BBox(-8.9, -43.0, 11.9, 1.0, -7.1, 7.2)
        assertEquals(expected, actual)
    }

    @Test
    fun `from should throw error when empty list is provided`() {
        shouldThrow<IllegalArgumentException> {
            BBox.from(listOf())
        }
    }
}
