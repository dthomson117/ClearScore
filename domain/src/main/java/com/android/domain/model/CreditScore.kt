package com.android.domain.model

data class CreditScore (
    val creditReportInfo: CreditReportInfo
) {
    companion object {
        fun default() = CreditScore(
            creditReportInfo = CreditReportInfo.default()
        )
    }
}