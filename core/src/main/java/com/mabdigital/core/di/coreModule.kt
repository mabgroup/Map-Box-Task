package com.mabdigital.core.di

import org.koin.core.module.Module

val coreModule = mutableListOf<Module>().apply {
    this.add(coreNetworkModuleDeclaration)
    this.add(coreDatabaseModule)
}
