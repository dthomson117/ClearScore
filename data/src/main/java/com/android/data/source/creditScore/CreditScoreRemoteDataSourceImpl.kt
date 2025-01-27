package com.android.data.source.creditScore

import com.android.data.api.ApiCallHandler
import com.android.data.api.ApiResult
import com.android.data.api.AppApi
import com.android.data.model.CreditScoreJson

class CreditScoreRemoteDataSourceImpl(
    private val appApi: AppApi,
    private val apiCallHandler: ApiCallHandler
) : CreditScoreRemoteDataSource {
    override suspend fun getCreditScore(): ApiResult<CreditScoreJson> =
        apiCallHandler.safeApiCall { appApi.getCreditScore() }
}