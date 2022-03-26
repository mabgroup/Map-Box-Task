package com.mabdigital.myapplicationinterview

import android.app.Application
import com.mabdigital.core.base.markers.InitializationClass
import com.mabdigital.myapplicationinterview.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class InitializationClassImp : InitializationClass {
    override fun initTools(myApp: Application) {
        initKoin(myApp)
        initialisedTimber()
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