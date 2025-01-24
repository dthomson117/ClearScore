package com.android.data.api

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Provides a generic way to handle API calls and their results
 */
class ApiCallHandler {
    fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ApiResult<T>> {
        return flow {
            emit(ApiResult.Loading)

            apiCall().let { response ->
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            emit(ApiResult.Success(it))
                        } ?: emit(ApiResult.ApiError.EmptyResponse)
                    } else {
                        Napier.w("API Error: ${response.code()}")
                        response.errorBody()?.let {
                            it.close()
                            emit(ApiResult.ApiError.ResponseError(response.message()))
                        }
                    }
                } catch (e: IOException) {
                    Napier.e(e.message.toString(), e)
                    emit(ApiResult.ApiError.IOError(e.message, e))
                } catch (e: HttpException) {
                    Napier.e(e.message.toString(), e)
                    emit(ApiResult.ApiError.HttpError(e.code(), e.message()))
                }
            }
        }
    }
}

sealed class ApiResult<out T> {
    data object Loading : ApiResult<Nothing>()
    data class Success<out T>(val data: T) : ApiResult<T>()

    sealed class ApiError : ApiResult<Nothing>() {
        data object EmptyResponse : ApiError()
        data class ResponseError(val message: String?, val cause: Throwable? = null) : ApiError()
        data class HttpError(val code: Int, val message: String?) : ApiError()
        data class IOError(val message: String?, val cause: Throwable? = null) : ApiError()
    }
}
