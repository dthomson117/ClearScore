package com.android.data.api

import io.mockk.coEvery
import io.mockk.every
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
    private val connectivityChecker: ConnectivityChecker = mockk(relaxed = true)

    private lateinit var sut: ApiCallHandler

    @Before
    fun setUp() {
        sut = ApiCallHandler(
            connectivityChecker = connectivityChecker
        )
    }

    @Test
    fun `safeApiCall SHOULD return Success WHEN response is successful and has content`() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            coEvery { apiCall.invoke() } returns Response.success("Success")

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.Success<*>>()
        }
    }

    @Test
    fun `safeApiCall SHOULD return EmptyResponse WHEN response is successful and has no content`() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            coEvery { apiCall.invoke() } returns Response.success(null)

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.EmptyResponse>()
        }
    }

    @Test
    fun `safeApiCall SHOULD return ResponseError WHEN response is not successful`() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            coEvery { apiCall.invoke() } returns
                    Response.error<String>(400, ResponseBody.create(null, ""))

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.ResponseError>()
        }
    }

    @Test(expected = IOException::class)
    fun `safeApiCall SHOULD return IOError WHEN api throws IOException`() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            coEvery { apiCall.invoke() } throws IOException("IOException")

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.IOError>()
        }
    }

    @Test(expected = HttpException::class)
    fun `safeApiCall SHOULD return HttpError WHEN api throws HttpException`() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns true
            coEvery { apiCall.invoke() } throws HttpException(
                Response.error<String>(400, ResponseBody.create(null, ""))
            )

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.HttpError>()
        }
    }

    @Test
    fun `safeApiCall SHOULD return ResponseError WHEN no connection`() {
        runTest {
            every { connectivityChecker.checkConnectivity() } returns false

            val result = sut.safeApiCall { apiCall() }

            expectThat(result).isA<ApiResult.ApiError.ResponseError>()
        }
    }
}