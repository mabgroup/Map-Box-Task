package com.mabdigital.core.presentation.router

import android.content.Context
import android.content.Intent
import android.os.Bundle


fun offerIntent(context:Context) = createIntent(
    context, IntentFilter_Offers,
    extras = Bundle().apply {
        //TODO fill later
    }
)

private fun createIntent(context: Context, action: String, extras: Bundle? = null) =
    Intent(action).setPackage(context.packageName).also { intent ->
        extras?.let { intent.putExtras(it) }
    }
