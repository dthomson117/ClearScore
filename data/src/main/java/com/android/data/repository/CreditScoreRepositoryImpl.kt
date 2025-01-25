package com.android.data.repository

import com.android.data.mapper.CreditScoreMapper
import com.android.data.source.CreditScoreRemoteDataSource
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [CreditScoreRepository], exposes Credit Score operations to the domain layer
 * Shortcut - Would have implemented a local data source to cache data, such as Room
 */
class CreditScoreRepositoryImpl(
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource,
    private val creditScoreMapper: CreditScoreMapper
) : CreditScoreRepository {
    override suspend fun getCreditScore(): Flow<RepositoryResult<CreditScore>> =
        apiCallToRepositoryResult(
            apiCall = { creditScoreRemoteDataSource.getCreditScore() },
            mapper = { creditScoreMapper.toDomain(it) }
        )
}
