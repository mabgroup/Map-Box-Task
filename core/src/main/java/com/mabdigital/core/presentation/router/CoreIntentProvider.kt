package com.mabdigital.core.presentation.router

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mabdigital.core.base.model.NotificationModel
import com.mabdigital.core.base.notification.NOTIFICATION_DATA


fun offerIntent(context:Context,modelData:NotificationModel? = null) = createIntent(
    context, IntentFilter_Offers,
    extras = Bundle().apply {
        putParcelable(NOTIFICATION_DATA,modelData)
    }
)

private fun createIntent(context: Context, action: String, extras: Bundle? = null) =
    Intent(action).setPackage(context.packageName).also { intent ->
        extras?.let { intent.putExtras(it) }
    }
