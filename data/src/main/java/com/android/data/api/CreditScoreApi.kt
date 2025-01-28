package com.android.data.api

import com.android.data.model.CreditScoreJson
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API for credit score
 */
interface CreditScoreApi {

    @GET(ENDPOINT)
    suspend fun getCreditScore(): Response<CreditScoreJson>

    companion object {
        const val BASE_URL = "https://android-interview.s3.eu-west-2.amazonaws.com/"
        const val ENDPOINT = "endpoint.json"
    }
}