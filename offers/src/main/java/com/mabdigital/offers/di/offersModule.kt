package com.mabdigital.offers.di


import org.koin.core.module.Module

val offersModule = mutableListOf<Module>().apply {
    this.add(toolsModule)
}
