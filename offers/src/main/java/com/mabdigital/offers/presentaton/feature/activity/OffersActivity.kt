package com.mabdigital.offers.presentaton.feature.activity

import android.os.Bundle
import com.mabdigital.core.base.view.BaseActivity
import com.mabdigital.offers.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class OffersActivity : BaseActivity() {

    private val mViewModel : OfferShareViewModel by viewModel()

    override fun navigationGraph(): Int = R.navigation.offers_main_graph

    override fun doOnCreate(savedInstanceState: Bundle?) {}
}