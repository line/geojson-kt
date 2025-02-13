package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionSerializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should serialize Position when valid BBox is provided`() {
        val position = Position(0.0, 1.1)
        val actual = mapper.writeValueAsString(position)
        val expected = "[0.0, 1.1]".replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize Position when valid BBox with alt is provided`() {
        val position = Position(0.0, 1.1, 2.3)
        val actual = mapper.writeValueAsString(position)
        val expected = "[0.0, 1.1, 2.3]".replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }
}
