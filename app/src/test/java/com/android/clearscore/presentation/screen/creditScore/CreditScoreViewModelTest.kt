package com.android.clearscore.presentation.screen.creditScore

import com.android.domain.model.CreditReportInfo
import com.android.domain.model.CreditScore
import com.android.domain.repository.CreditScoreRepository
import com.android.domain.repository.RepositoryResult
import com.clearscore.common_kotlin.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class CreditScoreViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val creditScoreRepository: CreditScoreRepository = mockk(relaxed = true)

    private lateinit var sut: CreditScoreViewModel

    @Test
    fun `getCreditScore SHOULD be called WHEN viewModel is initialized`() {
        manualSetUp()

        coVerify { creditScoreRepository.getCreditScore() }
    }

    @Test
    fun `getCreditScore SHOULD set loading to true WHEN result is loading`() {
        coEvery { creditScoreRepository.creditScore } returns MutableStateFlow(RepositoryResult.Loading())

        manualSetUp()

        expectThat(sut.uiState.value.error).isFalse()
        expectThat(sut.uiState.value.loading).isTrue()
        expectThat(sut.uiState.value.creditScore).isEqualTo(CreditScore.default())
    }

    @Test
    fun `getCreditScore SHOULD set error to true WHEN result is error`() {
        coEvery { creditScoreRepository.creditScore } returns MutableStateFlow(RepositoryResult.Error(Throwable("Error")))

        manualSetUp()

        expectThat(sut.uiState.value.error).isTrue()
        expectThat(sut.uiState.value.loading).isFalse()
        expectThat(sut.uiState.value.creditScore).isEqualTo(CreditScore.default())
    }

    @Test
    fun `getCreditScore SHOULD set creditScore WHEN result is success`() {
        coEvery { creditScoreRepository.creditScore } returns MutableStateFlow(RepositoryResult.Success(creditScore))

        manualSetUp()

        expectThat(sut.uiState.value.error).isFalse()
        expectThat(sut.uiState.value.loading).isFalse()
        expectThat(sut.uiState.value.creditScore).isEqualTo(creditScore)
    }

    @Test
    fun `getCreditScore SHOULD be called WHEN refresh is called`() {
        coEvery { creditScoreRepository.creditScore } returns MutableStateFlow(RepositoryResult.Loading<CreditScore>()).asStateFlow()

        manualSetUp()
        sut.handleUiEvent(CreditScoreUiEvent.Refresh)

        coVerify(exactly = 2) { creditScoreRepository.getCreditScore() }
    }

    private fun manualSetUp() {
        sut = CreditScoreViewModel(creditScoreRepository = creditScoreRepository)
    }

    private val creditScore = CreditScore(
        creditReportInfo = CreditReportInfo(
            score = 514,
            maxScoreValue = 700,
            minScoreValue = 0,
        )
    )
}
