package com.mabdigital.offers.di

import com.mabdigital.offers.presentaton.feature.activity.OfferShareViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { OfferShareViewModel() }
}