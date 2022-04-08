package com.mabdigital.core.base.notification

import com.google.firebase.messaging.RemoteMessage
import com.mabdigital.core.base.model.NotificationModel

class NotificationHelper(val remoteMessage: RemoteMessage) {

    fun parseToModel(): NotificationModel {
        val listString = mutableListOf<String>()
        val sourceInfo = remoteMessage.data["source"] ?: ""
        val destCount = remoteMessage.data["dest_count"] ?: "0"
        val price = remoteMessage.data["price"] ?: ""
        for (i in 1..destCount.toInt()) {
            val listArray = remoteMessage.data["dest_$i"] ?: ""
            if (listArray.isNotEmpty())
                listString.add(listArray)
        }
        return NotificationModel(
            sourceInfo,
            price,
            listString
        )
    }
}