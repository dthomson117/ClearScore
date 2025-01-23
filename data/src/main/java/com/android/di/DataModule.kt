package com.android.di

import com.android.data.api.AppApi
import com.android.data.api.AppApi.Companion.BASE_URL
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    single<AppApi> { buildRetrofitInstance() }
}

fun buildRetrofitInstance(): AppApi {
    return Retrofit.Builder()
        .client(buildOkHttpClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(AppApi::class.java)
}

fun buildOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}