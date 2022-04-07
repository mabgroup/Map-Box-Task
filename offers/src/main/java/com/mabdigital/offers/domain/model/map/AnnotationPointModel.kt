package com.mabdigital.offers.domain.model.map

import com.mapbox.geojson.Point

data class PointDetails(
    val point:Point,
    val type: TerminalLocationTypeEnum
)

enum class TerminalLocationTypeEnum {
    Unknown,
    Source,
    Destination
}