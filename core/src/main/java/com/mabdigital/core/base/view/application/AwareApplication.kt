package com.mabdigital.core.base.view.application

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.mabdigital.core.base.callbacks.ActiveActivityLifecycleCallbacks

open class AwareApplication : MultiDexApplication() {

    companion object {
        internal lateinit var INSTANCE : AwareApplication
    }

    private val activeActivityCallbacks = ActiveActivityLifecycleCallbacks()

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activeActivityCallbacks)
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(activeActivityCallbacks)
        super.onTerminate()
    }

    val activeActivity: Activity?
        get() = activeActivityCallbacks.getActiveActivity()

}