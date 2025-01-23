package com.android.domain.repository

import com.android.domain.model.CreditScore
import kotlinx.coroutines.flow.Flow

interface CreditScoreRepository {
    suspend fun getCreditScore(): Flow<RepositoryResult<CreditScore>>
}