package com.android.clearscore.presentation.screen.creditScore

import androidx.lifecycle.viewModelScope
import com.android.clearscore.common.BaseViewModel
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreditScoreViewModel(
    private val creditScoreRepository: CreditScoreRepository
) : BaseViewModel<CreditScoreViewModelState>(CreditScoreViewModelState()) {
    init {
        viewModelScope.launch {
            getCreditScore()
        }
    }

    fun handleUiEvent(event: CreditScoreUiEvent) {
        when (event) {
            is CreditScoreUiEvent.Refresh -> viewModelScope.launch { getCreditScore() }
        }
    }

    private suspend fun getCreditScore() {
        creditScoreRepository.getCreditScore().collect { creditScore ->
            when (creditScore) {
                is RepositoryResult.Loading -> setState { copy(loading = true) }
                is RepositoryResult.Error -> TODO()
                is RepositoryResult.Success -> setState {
                    copy(
                        creditScore = creditScore.data,
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
    val creditScore: CreditScore = CreditScore.default()
)