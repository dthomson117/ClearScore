package com.android.domain.repository

sealed class RepositoryResult<out T> {
    class Loading<T> : RepositoryResult<T>()
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error<T>(val throwable: Throwable) : RepositoryResult<T>()
    data class HttpError<T>(val throwable: Throwable, val code: Int, val message: String? = null) :
        RepositoryResult<T>()
}