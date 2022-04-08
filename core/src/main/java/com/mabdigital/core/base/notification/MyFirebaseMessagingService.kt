package com.mabdigital.core.base.notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mabdigital.core.presentation.feature.CoreActivity
import com.orhanobut.hawk.Hawk
import timber.log.Timber

internal const val FIREBASE_TOKEN = "com.mabdigital.myapplicationinterview.firebase_push"
const val NOTIFICATION_DATA = "Notification"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val TAG = "MyFirebaseMsgService"

        fun itsNewToken(token: String): Boolean {
            val cachedToken = Hawk.get(FIREBASE_TOKEN, "")
            return token != cachedToken
        }
    }

    override fun onNewToken(token: String) {
        cacheToken(token)
        Timber.d("FCM token:", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.wtf("Notification Is Here!")
        val messageData = NotificationHelper(remoteMessage)
        val model = messageData.parseToModel()
        val intent = Intent(this, CoreActivity::class.java).apply {
            putExtra(NOTIFICATION_DATA,model)
        }
        startActivity(intent)
    }


    private fun cacheToken(token: String) {

        Timber.d("FCM token:", token)
        Hawk.put(FIREBASE_TOKEN, token)
    }

}