package com.android.data.api

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okio.IOException
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import strikt.api.expectThat
import strikt.assertions.isA

class ApiCallHandlerTest {
    private val apiCall: suspend () -> Response<*> = mockk()

    private lateinit var sut: ApiCallHandler

    @Before
    fun setUp() {
        sut = ApiCallHandler()
    }

    @Test
    fun `safeApiCall SHOULD return Success WHEN response is successful and has content`() {
        runTest {
            coEvery { apiCall.invoke() } returns Response.success("Success")

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.Success<*>>()
        }
    }

    @Test
    fun `safeApiCall SHOULD return EmptyResponse WHEN response is successful and has no content`() {
        runTest {
            coEvery { apiCall.invoke() } returns Response.success(null)

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.EmptyResponse>()
        }
    }

    @Test
    fun `safeApiCall SHOULD return ResponseError WHEN response is not successful`() {
        runTest {
            coEvery { apiCall.invoke() } returns
                    Response.error<String>(400, ResponseBody.create(null, ""))

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.ResponseError>()
        }
    }

    @Test(expected = IOException::class)
    fun `safeApiCall SHOULD return IOError WHEN api throws IOException`() {
        runTest {
            coEvery { apiCall.invoke() } throws IOException("IOException")

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.IOError>()
        }
    }

    @Test(expected = HttpException::class)
    fun `safeApiCall SHOULD return HttpError WHEN api throws HttpException`() {
        runTest {
            coEvery { apiCall.invoke() } throws HttpException(
                Response.error<String>(400, ResponseBody.create(null, ""))
            )

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.HttpError>()
        }
    }
}