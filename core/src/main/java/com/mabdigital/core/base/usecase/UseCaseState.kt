package com.mabdigital.core.base.usecase

import com.mabdigital.core.base.model.NetworkErrorDetail
import com.mabdigital.core.base.model.ApiError as ErrorModel
typealias DomainResult<T> = (UseCaseState<T>)->Unit

sealed class UseCaseState<T> {
    class Loading<T>(val innerLoading:Boolean = false) : UseCaseState<T>()
    data class Success<T>(val data: T) : UseCaseState<T>()
    data class ApiError<T>(val error: ErrorModel) : UseCaseState<T>()
    data class Error<T>(val throwable: Throwable) : UseCaseState<T>()
    data class NetworkError<T>(val http: NetworkErrorDetail) : UseCaseState<T>()

}