package com.mabdigital.core.base.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationModel(
    val title: String? = "Notification",
    val body: String? = "Offers For You!",
    val price: String? = "0",
    val array: List<ExtraPoint>
) : Parcelable

@Parcelize
data class ExtraPoint(
    @SerializedName("pointx")
    val Latitude: Double,
    @SerializedName("pointy")
    val Longitude: Double,
    @SerializedName("address")
    val address: String? = "",
    @SerializedName("type")
    val type: String? = ""
) : Parcelable