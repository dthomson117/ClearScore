package com.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditScoreJson(
    val accountIDVStatus: String,
    val creditReportInfo: CreditReportInfoJson,
    val dashboardStatus: String,
    val personaType: String,
    val coachingSummary: CoachingSummaryJson,
    val augmentedCreditScore: Int?,
) {
    companion object {
        fun default() = CreditScoreJson(
            accountIDVStatus = "",
            creditReportInfo = CreditReportInfoJson.default(),
            dashboardStatus = "",
            personaType = "",
            coachingSummary = CoachingSummaryJson.default(),
            augmentedCreditScore = 0
        )
    }
}