package com.android.data.repository

import com.android.data.mapper.CreditScoreMapper
import com.android.data.source.creditScore.CreditScoreRemoteDataSource
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class CreditScoreRepositoryImpl(
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource,
    private val creditScoreMapper: CreditScoreMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CreditScoreRepository {
    private val _creditScore =
        MutableStateFlow<RepositoryResult<CreditScore>>(RepositoryResult.Loading())
    override val creditScore: StateFlow<RepositoryResult<CreditScore>> = _creditScore.asStateFlow()

    override suspend fun getCreditScore() {
        apiCallToRepositoryResult(
            apiCall = { creditScoreRemoteDataSource.getCreditScore() },
            mapper = { creditScoreMapper.toDomain(it) },
            dispatcher = dispatcher
        ).collect { result -> _creditScore.emit(result) }
    }
}
