package com.mabdigital.core.component.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mabdigital.core.base.view.application.AwareApplication
import com.mabdigital.core.base.view.dialog.showNetworkConnectionAlertDialog

class NetworkConnectionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        AwareApplication.INSTANCE.activeActivity?.let { it.showNetworkConnectionAlertDialog() }
    }
}