package com.android.clearscore.presentation.screen.creditScore

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.android.clearscore.R
import com.android.domain.model.CreditReportInfo
import com.android.domain.model.CreditScore
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class CreditScoreScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun loadingIndicator_SHOULD_show_WHEN_loading() {
        val uiState = CreditScoreViewModelState(loading = true)

        composeTestRule.setContent {
            CreditScoreScreen(uiState = uiState) {}
        }

        composeTestRule.onNodeWithTag(context.getString(R.string.loadingindicator))
            .assertIsDisplayed()
    }

    @Test
    fun creditScoreIndicator_SHOULD_show_WHEN_not_loading() {
        val uiState = CreditScoreViewModelState(loading = false, creditScore = creditScore)

        composeTestRule.setContent {
            CreditScoreScreen(uiState = uiState) {}
        }

        composeTestRule.onNodeWithTag(context.getString(R.string.creditscoreindicator))
            .assertIsDisplayed()
    }

    @Test
    fun creditScoreText_SHOULD_show_correct_text() {
        val uiState = CreditScoreViewModelState(loading = false, creditScore = creditScore)

        composeTestRule.setContent {
            CreditScoreScreen(uiState = uiState) {}
        }

        composeTestRule.onNodeWithText(creditScore.creditReportInfo.score.toString())
            .assertIsDisplayed()
            .assertTextContains(creditScore.creditReportInfo.score.toString())

        composeTestRule.onNodeWithText(context.getString(R.string.credit_score_indicator_top))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(
            context.getString(
                R.string.credit_score_indicator_bottom,
                creditScore.creditReportInfo.maxScoreValue
            )
        ).assertIsDisplayed()
    }


    private val creditScore = CreditScore(
        creditReportInfo = CreditReportInfo.default().copy(
            score = 300,
            maxScoreValue = 700
        )
    )

    @Test
    fun refresh_button_SHOULD_call_refresh_function_WHEN_pressed() {
        val uiState = CreditScoreViewModelState(loading = false, creditScore = creditScore, error = true)
        val callback: (CreditScoreUiEvent) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            CreditScoreScreen(uiState = uiState, handleUiEvent = callback)
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.refresh_cd))
            .assertIsDisplayed()
            .performClick()
        verify { callback(CreditScoreUiEvent.Refresh) }
    }
}
