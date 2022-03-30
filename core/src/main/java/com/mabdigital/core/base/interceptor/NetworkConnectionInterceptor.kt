package com.mabdigital.core.base.interceptor

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(private val cm: ConnectivityManager?) : Interceptor {

    @Suppress("DEPRECATION")
    private val isConnected: Boolean
        get() {
            var result = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NetworkConnectionException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

class NetworkConnectionException : IOException()