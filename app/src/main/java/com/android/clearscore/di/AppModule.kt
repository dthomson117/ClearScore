package com.android.clearscore.di

import com.android.clearscore.presentation.screen.creditScore.CreditScoreViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Viewmodel
    viewModel { CreditScoreViewModel(creditScoreRepository = get()) }
}