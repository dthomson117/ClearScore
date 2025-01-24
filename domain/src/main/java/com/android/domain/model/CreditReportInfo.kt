package com.android.domain.model

data class CreditReportInfo (
    val score: Int,
    val maxScoreValue: Int,
    val minScoreValue: Int,
) {
    companion object {
        fun default() = CreditReportInfo(
            score = 0,
            maxScoreValue = 0,
            minScoreValue = 0
        )
    }
}