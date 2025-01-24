package com.android.clearscore.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<T>(
    viewModelState: T
) : ViewModel() {
    private val _uiState = MutableStateFlow(viewModelState)
    val uiState: StateFlow<T> = _uiState.asStateFlow()

    fun setState(value: T.() -> T) = _uiState.update { value(it) }
}
