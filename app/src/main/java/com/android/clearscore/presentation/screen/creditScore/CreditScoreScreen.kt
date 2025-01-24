package com.android.clearscore.presentation.screen.creditScore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.domain.model.CreditReportInfo

@Composable
fun CreditScoreScreen(
    uiState: CreditScoreViewModelState,
    handleUiEvent: (CreditScoreUiEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.loading) {
            CircularProgressIndicator()
        } else {
            CreditScoreCircle(uiState.creditScore.creditReportInfo)
        }
    }
}

@Composable
fun CreditScoreCircle(creditReportInfo: CreditReportInfo) {
    CircularProgressIndicator(progress = { creditReportInfo.score.toFloat() / creditReportInfo.maxScoreValue })
}