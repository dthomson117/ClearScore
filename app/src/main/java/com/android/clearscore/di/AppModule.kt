package com.android.clearscore.di

import com.android.clearscore.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Viewmodel
    viewModel { MainViewModel() }
}