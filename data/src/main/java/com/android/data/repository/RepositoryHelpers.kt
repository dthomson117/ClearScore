package com.android.data.repository

import com.android.data.api.ApiResult
import com.android.domain.repository.RepositoryResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Helper function to convert an API call to a flow of [RepositoryResult]
 */
fun <ApiData, DomainData> apiCallToRepositoryResult(
    apiCall: suspend () -> ApiResult<ApiData>,
    mapper: (ApiData) -> DomainData,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Flow<RepositoryResult<DomainData>> = flow {
    emit(RepositoryResult.Loading())

    when (val apiResult = apiCall()) {
        is ApiResult.Success -> {
            try {
                val domainData = mapper(apiResult.data)
                emit(RepositoryResult.Success(domainData))
            } catch (e: Exception) {
                emit(RepositoryResult.Error(Throwable("Mapping Error: ${e.message}")))
            }
        }

        is ApiResult.ApiError.EmptyResponse -> {
            Napier.w("Empty Response")
            emit(RepositoryResult.Error(Throwable("Empty Response from ${apiCall.javaClass.simpleName}")))
        }

        is ApiResult.ApiError.HttpError -> {
            Napier.w("HTTP Error: ${apiResult.code} - ${apiResult.message}")
            emit(RepositoryResult.Error(Throwable("HTTP Error: ${apiResult.code} - ${apiResult.message}")))
        }

        is ApiResult.ApiError.IOError -> {
            Napier.w("IO Error: ${apiResult.message}")
            emit(RepositoryResult.Error(Throwable("IO Error: ${apiResult.message}")))
        }

        is ApiResult.ApiError.ResponseError -> {
            Napier.w("Response Error: ${apiResult.message}")
            emit(RepositoryResult.Error(Throwable("Response Error: ${apiResult.message}")))
        }
    }
}.flowOn(dispatcher)