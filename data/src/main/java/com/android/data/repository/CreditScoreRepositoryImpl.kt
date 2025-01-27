package com.android.data.repository

import com.android.data.mapper.CreditScoreMapper
import com.android.data.source.creditScore.CreditScoreRemoteDataSource
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [CreditScoreRepository], exposes Credit Score operations to the domain layer
 * Shortcut - Would have implemented a local data source to cache data, such as Room
 */
class CreditScoreRepositoryImpl(
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource,
    private val creditScoreMapper: CreditScoreMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CreditScoreRepository {
    override suspend fun getCreditScore(): Flow<RepositoryResult<CreditScore>> {
        return apiCallToRepositoryResult(
            apiCall = { creditScoreRemoteDataSource.getCreditScore() },
            mapper = { creditScoreMapper.toDomain(it) },
            dispatcher = dispatcher
        )
    }
}
