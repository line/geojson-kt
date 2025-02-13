package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import jp.co.lycorp.geojson.BBox
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class BBoxDeserializationTest {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `should deserialize to BBox when valid GeoJSON is provided()`() {
        val geoJson = "[0.0, 0.0, 1.0, 1.0]".replace("\\s".toRegex(), "")
        val actual = mapper.readValue(geoJson, BBox::class.java)
        val expected = BBox(0.0, 0.0, 1.0, 1.0)
        assertTrue(actual is BBox)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to BBox when valid GeoJSON with alt is provided`() {
        val geoJson = "[0.0, 0.0, -44.0, 1.0, 1.0, 44.0]".replace("\\s".toRegex(), "")
        val actual = mapper.readValue(geoJson, BBox::class.java)
        val expected = BBox(0.0, 0.0, 1.0, 1.0, -44.0, 44.0)
        assertTrue(actual is BBox)
        assertEquals(expected, actual)
    }

    @Test
    fun `should throw error when not array`() = runTest {
        val testCases = Exhaustive.collection(listOf("{}", "\"foobar\"", "17"))

        checkAll(testCases) {
            val exception = shouldThrow<IOException> {
                mapper.readValue(it, BBox::class.java)
            }

            exception.message.shouldContain("Unable to deserialize bbox: no array found")
        }
    }

    @Test
    fun `should throw error when array size is invalid`() = runTest {
        val testCases = Exhaustive.collection(listOf("[]", "[0.0, 0.1]", "[0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6]"))

        checkAll(testCases) {
            val exception = shouldThrow<IOException> {
                mapper.readValue(it, BBox::class.java)
            }

            exception.message.shouldContain("Unexpected coordinate array size:")
        }
    }
}
