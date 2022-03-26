package com.mabdigital.myapplicationinterview

import androidx.multidex.MultiDexApplication
import com.mabdigital.core.base.markers.InitializationClass

class MyApp : MultiDexApplication() {
    private val initializationClass: InitializationClass by lazy { InitializationClassImp() }
    override fun onCreate() {
        super.onCreate()
        initializationClass.initTools(this)
    }
}