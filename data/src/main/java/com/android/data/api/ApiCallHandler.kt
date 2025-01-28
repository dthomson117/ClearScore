package com.android.data.api

import com.squareup.moshi.JsonDataException
import io.github.aakira.napier.Napier
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Provides a generic way to handle API calls and their results
 */
class ApiCallHandler(
    private val connectivityChecker: ConnectivityChecker,
) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        if (!connectivityChecker.checkConnectivity()) {
            return ApiResult.ApiError.ResponseError("No internet connection")
        }

        apiCall().let { response ->
            return try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiResult.Success(it)
                    } ?: ApiResult.ApiError.EmptyResponse
                } else {
                    Napier.w("API Error: ${response.code()}")
                    ApiResult.ApiError.ResponseError(response.message())
                }
            } catch (e: IOException) {
                Napier.e(e.message.toString(), e)
                ApiResult.ApiError.IOError(e.message, e)
            } catch (e: HttpException) {
                Napier.e(e.message.toString(), e)
                ApiResult.ApiError.HttpError(e.code(), e.message())
            } catch (e: JsonDataException) {
                Napier.w("Invalid response: $response")
                ApiResult.ApiError.ResponseError(response.message())
            }
        }
    }
}

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()

    sealed class ApiError : ApiResult<Nothing>() {
        data object EmptyResponse : ApiError()
        data class ResponseError(val message: String?, val cause: Throwable? = null) : ApiError()
        data class HttpError(val code: Int, val message: String?) : ApiError()
        data class IOError(val message: String?, val cause: Throwable? = null) : ApiError()
    }
}
