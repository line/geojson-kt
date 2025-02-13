package jp.co.lycorp.geojson.validator

import jp.co.lycorp.geojson.LinearRing
import jp.co.lycorp.geojson.PolygonCoordinates
import jp.co.lycorp.geojson.Position
import jp.co.lycorp.geojson.algorithm.convexPolygonsIntersection

/**
 * PolygonValidator class
 *
 *　Validates that the given rings are in proper alignment with each other.
 */
internal object RingIntersectionValidator {
    fun validateRings(coordinates: PolygonCoordinates) {
        val exteriorRing = coordinates[0]

        // validate all interior rings
        coordinates.drop(1).forEach {
                interiorRing ->
            validateInteriorRingAgainstExterior(interiorRing, exteriorRing)
            validateInteriorRingsAgainstEachOther(interiorRing, coordinates.drop(1))
        }
    }

    private fun validateInteriorRingAgainstExterior(
        interiorRing: List<Position>,
        exteriorRing: List<Position>,
    ) {
        require(interiorRing.dropLast(1).all { LinearRing(exteriorRing).isPointInLinearRing(it) }) {
            "The interior ring of a Polygon must not intersect or cross the exterior ring."
        }
    }

    private fun validateInteriorRingsAgainstEachOther(
        targetInteriorRing: List<Position>,
        interiorRings: PolygonCoordinates,
    ) {
        interiorRings.forEach {
                nextInteriorRing ->
            if (targetInteriorRing != nextInteriorRing) {
                require(
                    !convexPolygonsIntersection(targetInteriorRing, nextInteriorRing) &&
                        nextInteriorRing.dropLast(1).all {
                            LinearRing(targetInteriorRing).isPointOutsideLinearRing(it)
                        },
                ) {
                    "No two interior rings may intersect, cross, or encompass each other"
                }
            }
        }
    }
}
