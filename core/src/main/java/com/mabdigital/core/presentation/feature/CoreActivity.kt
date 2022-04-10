package com.mabdigital.core.presentation.feature

import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.mabdigital.core.R
import com.mabdigital.core.base.view.BaseActivity
import com.mabdigital.core.presentation.router.offerIntent
import com.google.android.gms.tasks.OnCompleteListener
import com.mabdigital.core.base.model.NotificationModel
import com.mabdigital.core.base.notification.FIREBASE_TOKEN
import com.mabdigital.core.base.notification.MyFirebaseMessagingService
import com.mabdigital.core.base.notification.NOTIFICATION_DATA
import com.orhanobut.hawk.Hawk
import timber.log.Timber

class CoreActivity : BaseActivity() {

    override fun navigationGraph(): Int? = null

    override fun doOnCreate(savedInstanceState: Bundle?) {
        observeFireBaseToken()
        navigateToOffers()
    }

    private fun observeFireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result

            Log.d("CoreActivity", "FCM token: $token")

            val isNewToken = MyFirebaseMessagingService.itsNewToken(token)
            if (isNewToken)
                Timber.d("FCM token: ", token ?: " - ")
        })
    }

    fun navigateToOffers() {
        startActivity(offerIntent(baseContext, getIntentModel()))
        overridePendingTransition(0, 0)
    }

    private fun getIntentModel(): NotificationModel? {
        return intent?.extras?.run {
            getParcelable(NOTIFICATION_DATA)
        }
    }
}