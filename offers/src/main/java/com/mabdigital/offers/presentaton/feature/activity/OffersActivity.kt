package com.mabdigital.offers.presentaton.feature.activity

import android.os.Bundle
import com.mabdigital.core.base.view.BaseActivity
import com.mabdigital.offers.R
import com.mabdigital.offers.databinding.MapLoadViewBinding

class OffersActivity : BaseActivity() {

    override fun navigationGraph(): Int = R.navigation.offers_main_graph

    override fun doOnCreate(savedInstanceState: Bundle?) {

    }
}