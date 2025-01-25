package com.android.domain.repository

import com.android.domain.model.CreditScore
import kotlinx.coroutines.flow.Flow

/**
 * Provides operations for credit score data
 */
interface CreditScoreRepository {
    suspend fun getCreditScore(): Flow<RepositoryResult<CreditScore>>
}