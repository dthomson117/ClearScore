package com.android.clearscore.presentation.screen.creditScore

import androidx.lifecycle.viewModelScope
import com.android.clearscore.common.BaseViewModel
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreditScoreViewModel(
    private val creditScoreRepository: CreditScoreRepository,
) : BaseViewModel<CreditScoreViewModelState>(CreditScoreViewModelState()) {
    init {
        viewModelScope.launch {
            creditScoreRepository.getCreditScore()
            startCreditScoreListener()
        }
    }

    fun handleUiEvent(event: CreditScoreUiEvent) {
        when (event) {
            is CreditScoreUiEvent.Refresh -> viewModelScope.launch {
                creditScoreRepository.getCreditScore()
            }
        }
    }

    private suspend fun startCreditScoreListener() {
        creditScoreRepository.creditScore.collectLatest { creditScore ->
            when (creditScore) {
                is RepositoryResult.Loading -> setState { copy(loading = true) }
                is RepositoryResult.Error -> setState { copy(loading = false, error = true) }
                is RepositoryResult.Success -> setState {
                    copy(
                        creditScore = creditScore.data,
                        error = false,
                        loading = false
                    )
                }
            }
        }
    }
}

sealed interface CreditScoreUiEvent {
    data object Refresh : CreditScoreUiEvent
}

data class CreditScoreViewModelState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val creditScore: CreditScore = CreditScore.default(),
)
