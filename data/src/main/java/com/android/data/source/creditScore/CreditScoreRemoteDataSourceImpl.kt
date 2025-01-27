package com.android.data.source.creditScore

import com.android.data.api.ApiCallHandler
import com.android.data.api.ApiResult
import com.android.data.api.AppApi
import com.android.data.model.CreditScoreJson

/**
 * Data source that gets the credit score from the API
 */
class CreditScoreRemoteDataSource(
    private val appApi: AppApi,
    private val apiCallHandler: ApiCallHandler
) {
    suspend fun getCreditScore(): ApiResult<CreditScoreJson> =
        apiCallHandler.safeApiCall { appApi.getCreditScore() }
}