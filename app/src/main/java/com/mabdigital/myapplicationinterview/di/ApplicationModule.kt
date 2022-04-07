package com.mabdigital.myapplicationinterview.di

import com.mabdigital.core.di.coreModule
import com.mabdigital.offers.di.offersModule
import org.koin.core.module.Module

val applicationModule = mutableListOf<Module>().apply {
    this.addAll(coreModule)
    this.addAll(offersModule)
}