package jp.co.lycorp.geojson.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Creates [ObjectMapper] instance with a registered [GeoJsonModule].
 */
fun geojsonObjectMapper(): ObjectMapper = jacksonObjectMapper().registerModule(GeoJsonModule())
