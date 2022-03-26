package com.mabdigital.core.base.adapters

interface Mapper<out DOMAIN_MODEL, DATA_MODEL> {

    fun dataToDomainModel(input: DATA_MODEL): DOMAIN_MODEL

    fun transformDataListToDomainList(data: List<DATA_MODEL>): List<DOMAIN_MODEL> {
        return data.map { dataToDomainModel(it) }
    }
}