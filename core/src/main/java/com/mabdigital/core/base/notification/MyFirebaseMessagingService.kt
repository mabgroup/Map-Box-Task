package com.mabdigital.core.base.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orhanobut.hawk.Hawk
import timber.log.Timber

internal const val FIREBASE_TOKEN = "com.mabdigital.myapplicationinterview.firebase_push"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object{

        private const val TAG = "MyFirebaseMsgService"

        fun itsNewToken(token:String):Boolean{
            val cachedToken = Hawk.get(FIREBASE_TOKEN,"")
            return token != cachedToken
        }
    }

    override fun onNewToken(token: String) {
        cacheToken(token)
        Timber.d("FCM token:", token)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }


    private fun cacheToken(token: String) {

        Timber.d("FCM token:", token)
        Hawk.put(FIREBASE_TOKEN , token)
    }

}