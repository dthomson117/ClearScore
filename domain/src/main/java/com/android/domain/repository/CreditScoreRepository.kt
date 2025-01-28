package com.android.domain.repository

import com.android.domain.model.CreditScore
import kotlinx.coroutines.flow.StateFlow

/**
 * Implementation of [CreditScoreRepository], exposes Credit Score operations to the domain layer
 * Shortcut - Would have implemented a local data source to cache data, such as Room
 */
interface CreditScoreRepository {
    val creditScore: StateFlow<RepositoryResult<CreditScore>>

    suspend fun getCreditScore()
}