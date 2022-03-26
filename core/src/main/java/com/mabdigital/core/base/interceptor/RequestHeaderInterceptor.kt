package com.ehsanmohit.taaghche.base.interceptor

import android.content.Context
import com.mabdigital.core.BuildConfig

import okhttp3.Interceptor
import okhttp3.Response

class RequestHeaderInterceptor(
    val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        original.newBuilder()
            .addHeader("X-Api-Key", BuildConfig.API_KEY)
        chain.proceed(original)
        return chain.proceed(original)
    }
}