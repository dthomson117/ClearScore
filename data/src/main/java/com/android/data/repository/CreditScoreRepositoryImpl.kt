package com.android.data.repository

import com.android.data.api.ApiCallHandler
import com.android.data.api.AppApi
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreditScoreRepositoryImpl(
    private val appApi: AppApi,
    private val apiCallHandler: ApiCallHandler
) : CreditScoreRepository {
    override suspend fun getCreditScore(): Flow<RepositoryResult<CreditScore>> {
        return flow {
            RepositoryResult.Loading<CreditScore>()
            apiCallHandler.safeApiCall { appApi.getCreditScore() }
        }
    }
}