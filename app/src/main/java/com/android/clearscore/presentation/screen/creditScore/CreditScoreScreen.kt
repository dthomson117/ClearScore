package com.android.clearscore.presentation.screen.creditScore

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.clearscore.presentation.common.ScreenLoading
import com.android.clearscore.ui.theme.progressBrush
import com.android.clearscore.ui.theme.xlText
import com.android.domain.model.CreditReportInfo
import com.example.clearscore.R
import kotlin.math.roundToInt

@Composable
fun CreditScoreScreen(
    uiState: CreditScoreViewModelState,
    handleUiEvent: (CreditScoreUiEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = uiState.loading, label = "LoadingAnimation") {
            if (it) {
                ScreenLoading(modifier = Modifier.size(200.dp))
            } else {
                CircularScoreIndicator(uiState.creditScore.creditReportInfo)
            }
        }
    }
}

@Composable
fun CircularScoreIndicator(
    creditReportInfo: CreditReportInfo,
    modifier: Modifier = Modifier,
    indicatorBrush: Brush = progressBrush,
    outlineColor: Color = Color.Transparent,
) {
    val scoreAnimation = remember { Animatable(0f) }
    LaunchedEffect(scoreAnimation) {
        scoreAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Progress Arc
            drawArc(
                startAngle = -60f,
                sweepAngle = 300f * (scoreAnimation.value * creditReportInfo.getScorePercentage()),
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round),
                brush = indicatorBrush,
                size = Size(size.width, size.height),
                topLeft = Offset(0f, 0f)
            )

            // Outline
            drawCircle(
                color = outlineColor,
                radius = (size.width / 2) + 20f,
                style = Stroke(width = 5f),
            )
        }

        val animatedScoreColor by animateColorAsState(
            when (scoreAnimation.value) {
                in 0.0..0.25 -> Color.Red
                in 0.25..0.5 -> Color.Yellow
                else -> Color.Green
            },
            label = "ScoreColor"
        )

        CreditScoreText(
            maxScoreValue = creditReportInfo.maxScoreValue,
            score = (creditReportInfo.score * scoreAnimation.value).roundToInt(),
            scoreColor = animatedScoreColor
        )
    }
}

@Composable
fun CreditScoreText(
    maxScoreValue: Int,
    score: Int,
    scoreColor: Color,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.credit_score_indicator_top),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "$score",
            color = scoreColor,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = xlText
            )
        )
        Text(
            text = stringResource(R.string.credit_score_indicator_bottom, maxScoreValue),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun PreviewScoreIndicator() {
    val creditReportInfo = CreditReportInfo.default().copy(
        score = 700,
        maxScoreValue = 700
    )

    CircularScoreIndicator(
        creditReportInfo = creditReportInfo
    )
}
