package com.android.domain.repository

/**
 * Represents the result of a repository operation
 */
sealed class RepositoryResult<out T> {
    class Loading<T> : RepositoryResult<T>()
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error<T>(val throwable: Throwable) : RepositoryResult<T>()
}