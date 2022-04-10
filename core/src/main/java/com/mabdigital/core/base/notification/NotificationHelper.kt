package com.mabdigital.core.base.notification

import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mabdigital.core.base.model.ExtraPoint
import com.mabdigital.core.base.model.NotificationModel

class NotificationHelper(val remoteMessage: RemoteMessage) {
    private val gson = Gson()
    fun parseToModel(): NotificationModel {
        val listString = mutableListOf<ExtraPoint>()
        val sourceInfo = remoteMessage.data["source"] ?: ""
        val destCount = remoteMessage.data["dest_count"] ?: "0"
        val price = remoteMessage.data["price"] ?: ""
        for (i in 1..destCount.toInt()) {
            val json = remoteMessage.data["dest_$i"] ?: ""
            val model:ExtraPoint? = convertJsonToModel(json)
            if (model!=null)
                listString.add(model)
        }
        return NotificationModel(
            remoteMessage.data["title"],
            remoteMessage.data["body"],
            convertJsonToModel(sourceInfo),
            price,
            listString.toList()
        )
    }

    private fun convertJsonToModel(listArray: String): ExtraPoint? {
        return if(listArray.isNotEmpty()) {
            val type =object : TypeToken<ExtraPoint>(){}.type
            gson.fromJson<ExtraPoint>(listArray,type)
        } else
            null
    }
}