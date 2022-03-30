package com.mabdigital.myapplicationinterview

import com.mabdigital.core.base.markers.InitializationClass
import com.mabdigital.core.base.view.application.AwareApplication

class MyApp : AwareApplication() {

    private val initializationClass: InitializationClass by lazy { InitializationClassImp() }

    override fun onCreate() {
        super.onCreate()
        initializationClass.initTools(this)
    }
}