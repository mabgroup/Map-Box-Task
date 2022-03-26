package com.mabdigital.myapplicationinterview.di

import com.mabdigital.core.di.coreModule
import org.koin.core.module.Module

val applicationModule = mutableListOf<Module>().apply {
    this.addAll(coreModule)
}