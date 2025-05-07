package jp.co.lycorp.geojson.jackson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import jp.co.lycorp.geojson.Position
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class PositionDeserializationTest {
    private val mapper = geojsonObjectMapper()

    @Test
    fun `should deserialize to Position when valid GeoJSON is provided`() {
        val geoJson = "[0.0, 1.1]".replace("\\s".toRegex(), "")
        val actual = mapper.readValue(geoJson, Position::class.java)
        val expected = Position(0.0, 1.1)
        assertTrue(actual is Position)
        assertEquals(expected, actual)
    }

    @Test
    fun `should deserialize to Position when valid GeoJSON with alt is provided`() {
        val geoJson = "[0.0, 1.1, 2.3]".replace("\\s".toRegex(), "")
        val actual = mapper.readValue(geoJson, Position::class.java)
        val expected = Position(0.0, 1.1, 2.3)
        assertTrue(actual is Position)
        assertEquals(expected, actual)
    }

    @Test
    fun `should throw error when not array`() = runTest {
        val testCases = Exhaustive.collection(listOf("{}", "\"foobar\"", "17"))

        checkAll(testCases) {
            val exception = shouldThrow<IOException> {
                mapper.readValue(it, Position::class.java)
            }

            exception.message.shouldContain("Unable to deserialize Position: no array found")
        }
    }

    @Test
    fun `should throw error when array size is invalid`() = runTest {
        val testCases = Exhaustive.collection(listOf("[]", "[0.0]", "[0.0, 0.1, 0.2, 0.3]"))

        checkAll(testCases) {
            val exception = shouldThrow<IOException> {
                mapper.readValue(it, Position::class.java)
            }

            exception.message.shouldContain("Unexpected coordinate array size:")
        }
    }
}
