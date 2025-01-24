package com.android.data.repository

import com.android.data.api.ApiResult
import com.android.data.mapper.CreditScoreMapper
import com.android.data.source.CreditScoreRemoteDataSource
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of [CreditScoreRepository], exposes Credit Score operations to the domain layer
 */
class CreditScoreRepositoryImpl(
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource,
    private val creditScoreMapper: CreditScoreMapper
) : CreditScoreRepository {
    override suspend fun getCreditScore(): Flow<RepositoryResult<CreditScore>> =
        flow {
            emit(RepositoryResult.Loading())

            when (val apiResult = creditScoreRemoteDataSource.getCreditScore()) {
                is ApiResult.Success -> {
                    emit(RepositoryResult.Success(creditScoreMapper.toDomain(apiResult.data)))
                }

                is ApiResult.ApiError -> {
                    emit(RepositoryResult.Error(Throwable(apiResult.toString())))
                }
            }
        }
}
