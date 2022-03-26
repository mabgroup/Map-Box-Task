package com.mabdigital.core.base.model

import retrofit2.HttpException
import java.io.IOException

/***
 * Mohammad Ehsan Mohit
 * Source : https://proandroiddev.com/create-retrofit-calladapter-for-coroutines-to-handle-response-as-states-c102440de37a
 * Author : Ahmad El-Melegy
 */
sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: HttpException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}

//Just For Shorter use
//ApiError Just a sample and depended on the project it may be different
typealias GenericResponse<S> = NetworkResponse<S, ApiError>