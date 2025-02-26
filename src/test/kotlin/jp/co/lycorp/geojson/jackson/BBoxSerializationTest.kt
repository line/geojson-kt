package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.co.lycorp.geojson.BBox
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BBoxSerializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should serialize BBox when valid BBox is provided`() {
        val bbox = BBox(0.0, 0.0, 1.0, 1.0)
        val actual = mapper.writeValueAsString(bbox)
        val expected = "[0.0, 0.0, 1.0, 1.0]".replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }

    @Test
    fun `should serialize BBox when valid BBox with alt is provided`() {
        val bbox = BBox(0.0, 0.0, 1.0, 1.0, -44.0, 44.0)
        val actual = mapper.writeValueAsString(bbox)
        val expected = "[0.0, 0.0, -44.0, 1.0, 1.0, 44.0]".replace("\\s".toRegex(), "")
        assertEquals(expected, actual)
    }
}
