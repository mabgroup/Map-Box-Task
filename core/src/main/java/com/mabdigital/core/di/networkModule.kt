package com.mabdigital.core.di

import android.content.Context
import android.net.ConnectivityManager
import com.ehsanmohit.taaghche.base.interceptor.RequestHeaderInterceptor
import com.google.gson.GsonBuilder
import com.mabdigital.core.BuildConfig
import com.mabdigital.core.base.connectionlivedata.ConnectionDetectionLiveData
import com.mabdigital.core.base.interceptor.NetworkConnectionInterceptor
import com.mabdigital.core.base.remote.adapters.NetworkResponseAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TEN_SECONDS = 10L
val coreNetworkModuleDeclaration = module {
    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    single { provideNetworkConnectionInterceptor(get()) }
    single {
        ConnectionDetectionLiveData(get())
    }
    single { provideOkHttpClient(context = androidContext(), networkConnectionInterceptor = get()) }
    single { provideRetrofit(okHttpClient = get(), BASE_URL = BuildConfig.BASE_URL) }
}

private fun provideNetworkConnectionInterceptor(connectivityManager: ConnectivityManager) =
    NetworkConnectionInterceptor(connectivityManager)

private fun provideOkHttpClient(
    context: Context,
    networkConnectionInterceptor: NetworkConnectionInterceptor
): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TEN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TEN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TEN_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(RequestHeaderInterceptor(context))
        .addInterceptor(networkConnectionInterceptor)
        .followRedirects(false)

    if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addNetworkInterceptor(interceptor)
    }

    return okHttpClient.build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
}