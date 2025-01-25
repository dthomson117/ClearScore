package com.android.data.repository

import com.android.data.mapper.CreditScoreMapper
import com.android.data.source.CreditScoreRemoteDataSource
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class CreditScoreRepositoryImplTest {
    private val creditScoreMapper: CreditScoreMapper = mockk()
    private val creditScoreRemoteDataSource: CreditScoreRemoteDataSource = mockk()

    private lateinit var sut: CreditScoreRepositoryImpl

    @Before
    fun setUp() {
        sut = CreditScoreRepositoryImpl(
            creditScoreRemoteDataSource = creditScoreRemoteDataSource,
            creditScoreMapper = creditScoreMapper
        )
    }

    @Test
    fun `getCreditScore SHOULD return CreditScore WHEN ApiResult is Success`() {

    }

    @Test
    fun `getCreditScore SHOULD return  WHEN ApiResult is Success`() {

    }
}