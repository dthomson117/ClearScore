package com.android.domain.model

data class CreditReportInfo(
    val score: Int,
    val maxScoreValue: Int,
    val minScoreValue: Int,
) {
    fun getScorePercentage(): Float {
        return if (maxScoreValue == 0) {
            0f
        } else {
            score.toFloat() / maxScoreValue
        }
    }

    companion object {
        fun default() = CreditReportInfo(
            score = 0,
            maxScoreValue = 0,
            minScoreValue = 0,
        )
    }
}