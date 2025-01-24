package com.android.clearscore.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.clearscore.presentation.screen.creditScore.CreditScoreScreen
import com.android.clearscore.presentation.screen.creditScore.CreditScoreViewModel
import com.android.clearscore.ui.theme.ClearScoreTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    // Shortcut: No navigation etc.

    private val creditScoreViewModel: CreditScoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState = creditScoreViewModel.uiState.collectAsStateWithLifecycle()
            ClearScoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        CreditScoreScreen(
                            uiState = uiState.value,
                            handleUiEvent = creditScoreViewModel::handleUiEvent
                        )
                    }
                }
            }
        }
    }
}
