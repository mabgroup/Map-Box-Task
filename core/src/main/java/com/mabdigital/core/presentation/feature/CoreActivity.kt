package com.mabdigital.core.presentation.feature

import android.os.Bundle
import com.mabdigital.core.R
import com.mabdigital.core.base.view.BaseActivity
import com.mabdigital.core.presentation.router.offerIntent

class CoreActivity : BaseActivity() {

    override fun navigationGraph(): Int {
        return R.navigation.start_up_nav
    }

    override fun doOnCreate(savedInstanceState: Bundle?) {}

    fun navigateToOffers() {
        startActivity(offerIntent(baseContext))
        overridePendingTransition(0, 0)
    }
}