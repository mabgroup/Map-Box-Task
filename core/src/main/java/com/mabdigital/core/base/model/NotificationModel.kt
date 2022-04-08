package com.mabdigital.core.base.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationModel(
    val source:String,
    val price:String,
    val array : List<String>
):Parcelable