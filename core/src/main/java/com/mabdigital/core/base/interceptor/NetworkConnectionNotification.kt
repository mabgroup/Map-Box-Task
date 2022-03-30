package com.mabdigital.core.base.interceptor

import android.content.Intent
import com.mabdigital.core.base.view.application.AwareApplication
import com.mabdigital.core.tools.Constants

internal fun networkConnectionNotification(){
    Intent().also { intent ->
        intent.action = "${Constants.NETWORK_CONNECTION_ALERT}"
        AwareApplication.INSTANCE.activeActivity?.let { activity ->
            intent.setPackage(activity.packageName)
            activity.sendBroadcast(intent)
        }

    }
}