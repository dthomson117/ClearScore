package com.android.data.repository

import app.cash.turbine.test
import com.android.data.api.ApiResult
import com.android.data.mapper.CreditScoreMapper
import com.android.data.model.CreditScoreJson
import com.android.data.source.CreditScoreRemoteDataSource
import com.android.domain.model.CreditScore
import com.android.domain.repository.RepositoryResult
import com.clearscore.common_kotlin.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class CreditScoreRepositoryImplTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val creditScoreMapper: CreditScoreMapper = mockk()
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource = mockk()

    private lateinit var sut: CreditScoreRepositoryImpl

    @Before
    fun setUp() {
        sut = CreditScoreRepositoryImpl(
            creditScoreRemoteDataSource = creditScoreRemoteDataSource,
            creditScoreMapper = creditScoreMapper,
            dispatcher = testCoroutineRule.dispatcher
        )
    }

    @Test
    fun `getCreditScore SHOULD return Loading and then CreditScore WHEN ApiResult is Success`() {
        runTest {
            every { creditScoreMapper.toDomain(CreditScoreJson.default()) } returns CreditScore.default()
            coEvery { creditScoreRemoteDataSource.getCreditScore() } returns ApiResult.Success(CreditScoreJson.default())

            sut.getCreditScore().test {
                val firstResult = awaitItem()
                val secondResult = awaitItem()

                expectThat(firstResult).isA<RepositoryResult.Loading<*>>()
                expectThat(secondResult).isA<RepositoryResult.Success<*>>()
                expectThat(secondResult).isEqualTo(RepositoryResult.Success(CreditScore.default()))
                awaitComplete()
            }
        }
    }

    @Test
    fun `getCreditScore SHOULD return Loading and then Error WHEN ApiResult is EmptyResponse`() {
        runTest {
            every { creditScoreMapper.toDomain(CreditScoreJson.default()) } returns CreditScore.default()
            coEvery { creditScoreRemoteDataSource.getCreditScore() } returns ApiResult.ApiError.EmptyResponse

            sut.getCreditScore().test {
                val firstResult = awaitItem()
                val secondResult = awaitItem()

                expectThat(firstResult).isA<RepositoryResult.Loading<*>>()
                expectThat(secondResult).isA<RepositoryResult.Error<*>>()
                awaitComplete()
            }
        }
    }

    @Test
    fun `getCreditScore SHOULD return Loading and then Error WHEN ApiResult is ResponseError`() {
        runTest {
            every { creditScoreMapper.toDomain(CreditScoreJson.default()) } returns CreditScore.default()
            coEvery { creditScoreRemoteDataSource.getCreditScore() } returns ApiResult.ApiError.ResponseError("Error")

            sut.getCreditScore().test {
                val firstResult = awaitItem()
                val secondResult = awaitItem()

                expectThat(firstResult).isA<RepositoryResult.Loading<*>>()
                expectThat(secondResult).isA<RepositoryResult.Error<*>>()
                awaitComplete()
            }
        }
    }

    @Test
    fun `getCreditScore SHOULD return Loading and then Error WHEN ApiResult is IOError`() {
        runTest {
            every { creditScoreMapper.toDomain(CreditScoreJson.default()) } returns CreditScore.default()
            coEvery { creditScoreRemoteDataSource.getCreditScore() } returns ApiResult.ApiError.IOError("Error")

            sut.getCreditScore().test {
                val firstResult = awaitItem()
                val secondResult = awaitItem()

                expectThat(firstResult).isA<RepositoryResult.Loading<*>>()
                expectThat(secondResult).isA<RepositoryResult.Error<*>>()
                awaitComplete()
            }
        }
    }

    @Test
    fun `getCreditScore SHOULD return Loading and then Error WHEN ApiResult is HttpError`() {
        runTest {
            every { creditScoreMapper.toDomain(CreditScoreJson.default()) } returns CreditScore.default()
            coEvery { creditScoreRemoteDataSource.getCreditScore() } returns ApiResult.ApiError.HttpError(400, "Error")

            sut.getCreditScore().test {
                val firstResult = awaitItem()
                val secondResult = awaitItem()

                expectThat(firstResult).isA<RepositoryResult.Loading<*>>()
                expectThat(secondResult).isA<RepositoryResult.Error<*>>()
                awaitComplete()
            }
        }
    }
}