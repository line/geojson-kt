package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import jp.co.lycorp.geojson.BBox

internal class BBoxDeserializer : StdDeserializer<BBox>(
    BBox::class.java,
) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): BBox {
        if (!p.isExpectedStartArrayToken) {
            ctxt.handleUnexpectedToken(
                BBox::class.java,
                p.currentToken,
                p,
                "Unable to deserialize bbox: no array found",
            )
        }

        val coords: List<Double> = p.readValueAs(object : TypeReference<List<Double>>() {})
        return when (coords.size) {
            COORDINATES_SIZE_2D -> BBox(
                minLng = coords[0],
                minLat = coords[1],
                maxLng = coords[2],
                maxLat = coords[3],
            )
            COORDINATES_SIZE_3D -> BBox(
                minLng = coords[0],
                minLat = coords[1],
                minAlt = coords[2],
                maxLng = coords[3],
                maxLat = coords[4],
                maxAlt = coords[5],
            )
            else -> {
                ctxt.handleUnexpectedToken(
                    BBox::class.java,
                    p.currentToken,
                    p,
                    "Unexpected coordinate array size: ${coords.size}",
                )
                error("coordinate array size is ${coords.size}")
            }
        }
    }
    companion object {
        const val COORDINATES_SIZE_2D = 4
        const val COORDINATES_SIZE_3D = 6
    }
}
