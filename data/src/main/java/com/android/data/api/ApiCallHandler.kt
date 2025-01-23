package com.android.data.api

import com.android.domain.repository.RepositoryResult
import retrofit2.HttpException
import java.io.IOException

class ApiCallHandler {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): RepositoryResult<T> {
        return try {
            RepositoryResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> RepositoryResult.Error(throwable)
                is HttpException -> {
                    val code = throwable.code()
                    RepositoryResult.HttpError(throwable = throwable, code = code)
                }

                else -> {
                    RepositoryResult.Error(throwable)
                }
            }
        }
    }
}
