package com.mabdigital.core.base.remote

import com.mabdigital.core.base.adapters.Mapper
import com.mabdigital.core.base.interceptor.NetworkConnectionException
import com.mabdigital.core.base.interceptor.networkConnectionNotification
import com.mabdigital.core.base.markers.DataModel
import com.mabdigital.core.base.model.ApiError
import com.mabdigital.core.base.model.GenericResponse
import com.mabdigital.core.base.model.NetworkErrorDetail
import com.mabdigital.core.base.model.NetworkResponse
import com.mabdigital.core.base.usecase.DomainResult
import com.mabdigital.core.base.usecase.UseCaseState
import retrofit2.HttpException
import timber.log.Timber

class NetworkBoundResources<T : Any, SR, M : Mapper<SR, T>>(
    private val response: GenericResponse<T>,
    private val result: DomainResult<SR>? = null,
    private val mapper: M? = null,
    private val onSuccess: ((T) -> Unit)? = null,
    private val onApiError: ((ApiError) -> Unit)? = null,
    private val onNetworkError: ((UseCaseState.NetworkError<SR>) -> Unit)? = null,
    private val onUndefinedError: ((Throwable) -> Unit)? = null
) {
    init {
        readResponse()
    }

    private fun readResponse() {
        Timber.d(Thread.currentThread().name)
        when (response) {
            is NetworkResponse.ApiError -> {
                response.body?.let {
                    result?.invoke(UseCaseState.ApiError(it))
                    onApiError?.invoke(it)
                }
            }
            is NetworkResponse.NetworkError -> {
                response.error?.let {
                    handleHttpError(it, result, onNetworkError)
                }
            }
            is NetworkResponse.Success -> {
                mappedToJsonModelAndNotify(response.body)
                response.body?.let { onSuccess?.invoke(it) }
            }
            is NetworkResponse.UnknownError -> {
                handleUndefinedError(response.error)
            }
        }
    }

    private fun mappedToJsonModelAndNotify(data: T) {
        val mappedData = when (data) {
            is DataModel -> mapper?.dataToDomainModel(data)
            else -> Unit
        } as SR
        result?.invoke(UseCaseState.Success(mappedData))
    }

    private fun handleHttpError(
        e: HttpException,
        callback: ((UseCaseState<SR>) -> Unit)?,
        onNetworkError: ((UseCaseState.NetworkError<SR>) -> Unit)? = null
    ) {
        val detail = UseCaseState.NetworkError<SR>(NetworkErrorDetail(e.code(), e.message()))
        callback?.invoke(detail)
        onNetworkError?.invoke(detail)
        e.printStackTrace()
    }

    private fun handleUndefinedError(e: Throwable?) {
        e?.let {
            if(e is NetworkConnectionException) {
                networkConnectionNotification()
            }else {
                result?.invoke(UseCaseState.Error(it))
                onUndefinedError?.invoke(it)
                it.printStackTrace()
            }
        }
    }
}