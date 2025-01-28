package com.android.clearscore.presentation.screen.creditScore

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.android.clearscore.R
import com.android.clearscore.presentation.common.ScreenLoading
import com.android.clearscore.ui.theme.ClearScoreTheme
import com.android.clearscore.ui.theme.creditScoreInset
import com.android.clearscore.ui.theme.creditScoreSize
import com.android.clearscore.ui.theme.progressBrush
import com.android.clearscore.ui.theme.thinStroke
import com.android.clearscore.ui.theme.xThickStroke
import com.android.clearscore.ui.theme.xlText
import com.android.domain.model.CreditReportInfo
import com.android.domain.model.CreditScore
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
                ScreenLoading(modifier = Modifier.size(creditScoreSize))
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularScoreIndicator(uiState.creditScore.creditReportInfo)
                    if (uiState.error) {
                        CreditScoreError(handleUiEvent = handleUiEvent)
                    }
                }
            }
        }
    }
}

@Composable
fun CircularScoreIndicator(
    creditReportInfo: CreditReportInfo,
    modifier: Modifier = Modifier,
    indicatorBrush: Brush = progressBrush,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    outlineVisible: Boolean = false,
    outlineColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    outlineGap: Float = 20f
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
        modifier = modifier
            .size(creditScoreSize)
            .testTag(stringResource(R.string.creditscoreindicator))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            inset(horizontal = creditScoreInset, vertical = creditScoreInset) {
                val trackStart = -60f
                val trackEnd = 300f

                // Track
                drawArc(
                    startAngle = trackStart,
                    sweepAngle = trackEnd,
                    useCenter = false,
                    style = Stroke(width = xThickStroke, cap = StrokeCap.Round),
                    color = trackColor,
                    size = Size(size.width, size.height),
                    topLeft = Offset(0f, 0f)
                )

                // Progress Arc
                val progress =
                    trackEnd * (scoreAnimation.value * creditReportInfo.getScorePercentage())
                drawArc(
                    startAngle = trackStart,
                    sweepAngle = progress,
                    useCenter = false,
                    style = Stroke(width = xThickStroke, cap = StrokeCap.Round),
                    brush = indicatorBrush,
                    size = Size(size.width, size.height),
                    topLeft = Offset(0f, 0f)
                )

                // Outline
                if (outlineVisible) {
                    drawCircle(
                        color = outlineColor,
                        radius = (size.width / 2) + outlineGap,
                        style = Stroke(width = thinStroke),
                    )
                }
            }
        }

        val animatedScoreColor by animateColorAsState(
            targetValue = when (scoreAnimation.value * creditReportInfo.getScorePercentage()) {
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

@Composable
fun CreditScoreError(
    color: Color = MaterialTheme.colorScheme.onSurface,
    handleUiEvent: (CreditScoreUiEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.error_credit_score),
            textAlign = TextAlign.Center,
            color = color
        )
        IconButton(
            onClick = { handleUiEvent(CreditScoreUiEvent.Refresh) }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                tint = color,
                contentDescription = stringResource(R.string.refresh_cd)
            )
        }
    }
}

@Preview
@Composable
fun PreviewScoreIndicator() {
    val creditReportInfo = CreditReportInfo.default().copy(
        score = 700,
        maxScoreValue = 700
    )

    ClearScoreTheme {
        Surface {
            CircularScoreIndicator(
                creditReportInfo = creditReportInfo
            )
        }
    }
}

@Preview
@Composable
fun PreviewError() {
    ClearScoreTheme {
        Surface {
            CreditScoreError(handleUiEvent = {})
        }
    }
}

@Preview
@Composable
fun PreviewScreen() {
    val uiState = CreditScoreViewModelState(
        loading = false,
        error = false,
        creditScore = CreditScore.default()
    )

    ClearScoreTheme {
        Surface {
            CreditScoreScreen(
                uiState = uiState,
                handleUiEvent = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewScreenLoading() {
    val uiState = CreditScoreViewModelState(
        loading = true,
        error = false,
        creditScore = CreditScore.default()
    )

    ClearScoreTheme {
        Surface {
            CreditScoreScreen(
                uiState = uiState,
                handleUiEvent = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewScreenError() {
    val uiState = CreditScoreViewModelState(
        loading = false,
        error = true,
        creditScore = CreditScore.default()
    )

    ClearScoreTheme {
        Surface {
            CreditScoreScreen(
                uiState = uiState,
                handleUiEvent = {}
            )
        }
    }
}
