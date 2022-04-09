package com.mabdigital.myapplicationinterview

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mabdigital.core.base.markers.InitializationClass
import com.mabdigital.core.base.notification.CHANNEL_ID
import com.mabdigital.myapplicationinterview.di.applicationModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class InitializationClassImp : InitializationClass {
    override fun initTools(myApp: Application) {
        initKoin(myApp)
        initialisedTimber()
        initialiseHawk(myApp)
        createNotificationChannel(myApp)
    }

    private fun createNotificationChannel(myApp: Application) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = myApp.getString(R.string.default_notification_channel_id)
            val descriptionText = myApp.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                myApp.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initialiseHawk(myApp: Application) {
        Hawk.init(myApp).build()
    }

    private fun initialisedTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun initKoin(myApp: Application) {
        startKoin {
            androidContext(myApp)
            modules(applicationModule)
        }
    }
}