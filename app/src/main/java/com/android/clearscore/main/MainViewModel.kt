package com.android.clearscore.main

import com.android.clearscore.common.BaseViewModel
import com.android.domain.model.CreditScore

class MainViewModel : BaseViewModel<MainViewModelState>(MainViewModelState())

data class MainViewModelState(
    val creditScore: CreditScore = CreditScore.default()
)