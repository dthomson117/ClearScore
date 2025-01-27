package com.android.di

import androidx.annotation.VisibleForTesting
import com.android.data.api.ApiCallHandler
import com.android.data.api.AppApi
import com.android.data.api.AppApi.Companion.BASE_URL
import com.android.data.mapper.CreditReportInfoMapper
import com.android.data.mapper.CreditScoreMapper
import com.android.data.repository.CreditScoreRepositoryImpl
import com.android.data.source.creditScore.CreditScoreRemoteDataSource
import com.android.data.source.creditScore.CreditScoreRemoteDataSourceImpl
import com.android.domain.repository.CreditScoreRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    // Api
    single<OkHttpClient> { buildOkHttpClient() }
    single<AppApi> { buildRetrofitInstance(client = get()) }
    factory { ApiCallHandler() }

    // Mapper
    factory { CreditReportInfoMapper() }
    factory { CreditScoreMapper(creditReportInfoMapper = get()) }

    // Repository
    single<CreditScoreRepository> {
        CreditScoreRepositoryImpl(
            creditScoreRemoteDataSource = get(),
            creditScoreMapper = get()
        )
    }

    // Data Source
    single<CreditScoreRemoteDataSource> {
        CreditScoreRemoteDataSourceImpl(
            appApi = get(),
            apiCallHandler = get()
        )
    }
}

@VisibleForTesting
fun buildRetrofitInstance(client: OkHttpClient, baseUrl: String = BASE_URL): AppApi {
    return Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(buildMoshi()))
        .build()
        .create(AppApi::class.java)
}

@VisibleForTesting
fun buildOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}

@VisibleForTesting
fun buildMoshi(): Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()