package com.mabdigital.core.base.markers

interface ResponseModel

interface DomainModel

interface DataModel {
    fun toDomainModel() : DomainModel
}