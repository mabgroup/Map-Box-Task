package com.mabdigital.core.base.notification

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mabdigital.core.R
import com.mabdigital.core.base.model.NotificationModel
import com.mabdigital.core.presentation.feature.CoreActivity
import com.orhanobut.hawk.Hawk
import timber.log.Timber
import kotlin.random.Random

internal const val FIREBASE_TOKEN = "com.mabdigital.myapplicationinterview.firebase_push"
const val CHANNEL_ID = "com.mabdigital.myapplicationinterview"
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
        makeNotification(model)
    }

    private fun makeNotification(model: NotificationModel) {

        val intent = Intent(this, CoreActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(NOTIFICATION_DATA, model)
        }
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_textsms_24)
            .setContentTitle(model.title)
            .setContentText(model.body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(Random.nextInt(),builder.build())
        }

    }


    private fun cacheToken(token: String) {

        Timber.d("FCM token:", token)
        Hawk.put(FIREBASE_TOKEN, token)
    }

}