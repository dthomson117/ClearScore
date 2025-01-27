package com.android.data.source.creditScore

import com.android.data.api.ApiResult
import com.android.data.model.CreditScoreJson

/**
 * Data source that gets the credit score from the API
 */
interface CreditScoreRemoteDataSource {
    suspend fun getCreditScore(): ApiResult<CreditScoreJson>
}