package com.mabdigital.core.base.callbacks

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActiveActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private var activeActivity: Activity? = null

    fun getActiveActivity(): Activity? = activeActivity

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activeActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        /*NO OP*/
    }

    override fun onActivityResumed(activity: Activity) {
        /*NO OP*/
    }

    override fun onActivityPaused(activity: Activity) {
        /*NO OP*/
    }

    override fun onActivityStopped(activity: Activity) {
        /*NO OP*/
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        /*NO OP*/
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity === activeActivity) {
            activeActivity = null
        }
    }
}