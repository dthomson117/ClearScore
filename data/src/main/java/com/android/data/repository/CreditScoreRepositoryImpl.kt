package com.android.data.repository

import com.android.data.api.ApiResult
import com.android.data.mapper.CreditScoreMapper
import com.android.data.source.CreditScoreRemoteDataSource
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult

/**
 * Implementation of [CreditScoreRepository], exposes Credit Score operations to the domain layer
 */
class CreditScoreRepositoryImpl(
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource,
    private val creditScoreMapper: CreditScoreMapper
) : CreditScoreRepository {
    override suspend fun getCreditScore(): RepositoryResult<CreditScore> =
        when (val apiResult = creditScoreRemoteDataSource.getCreditScore()) {
            is ApiResult.Loading -> {
                RepositoryResult.Loading()
            }

            is ApiResult.Success -> {
                RepositoryResult.Success(creditScoreMapper.toDomain(apiResult.data))
            }

            is ApiResult.ApiError -> {
                RepositoryResult.Error(Throwable(apiResult.toString()))
            }
        }
}