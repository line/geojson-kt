package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import jp.co.lycorp.geojson.Position

internal class PositionDeserializer : StdDeserializer<Position>(Position::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Position {
        if (!p.isExpectedStartArrayToken) {
            ctxt.handleUnexpectedToken(
                Position::class.java,
                p.currentToken,
                p,
                "Unable to deserialize Position: no array found",
            )
        }

        val coords: List<Double> = p.readValueAs(object : TypeReference<List<Double>>() {})
        return when (coords.size) {
            COORDINATES_SIZE_2D -> Position(
                lng = coords[0],
                lat = coords[1],
            )
            COORDINATES_SIZE_3D -> Position(
                lng = coords[0],
                lat = coords[1],
                alt = coords[2],
            )
            else -> {
                ctxt.handleUnexpectedToken(
                    Position::class.java,
                    p.currentToken,
                    p,
                    "Unexpected coordinate array size: ${coords.size}",
                )
                error("coordinate array size is ${coords.size}")
            }
        }
    }

    companion object {
        const val COORDINATES_SIZE_2D = 2
        const val COORDINATES_SIZE_3D = 3
    }
}
