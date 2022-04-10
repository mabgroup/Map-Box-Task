package com.mabdigital.offers.domain.model.map

import android.os.Parcelable
import com.mapbox.geojson.Point
import kotlinx.parcelize.Parcelize

@Parcelize
data class PointDetails(
    val point:Point,
    val pointAddress:String,
    val type: TerminalLocationTypeEnum
) : Parcelable

enum class TerminalLocationTypeEnum {
    Unknown,
    Source,
    Destination;

    companion object {
        fun toType(name: String): TerminalLocationTypeEnum = when (name.lowercase()) {
            Source.name.lowercase() -> Source
            Destination.name.lowercase() -> Destination
            else -> Unknown
        }
    }
}