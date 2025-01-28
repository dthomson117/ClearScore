package com.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditReportInfoJson(
    val score: Int,
    val scoreBand: Int,
    val clientRef: String,
    val status: String,
    val maxScoreValue: Int,
    val minScoreValue: Int,
    val monthsSinceLastDefaulted: Int,
    val hasEverDefaulted: Boolean,
    val monthsSinceLastDelinquent: Int,
    val hasEverBeenDelinquent: Boolean,
    val percentageCreditUsed: Int,
    val percentageCreditUsedDirectionFlag: Int,
    val changedScore: Int,
    val currentShortTermDebt: Int,
    val currentShortTermNonPromotionalDebt: Int,
    val currentShortTermCreditLimit: Int,
    val currentShortTermCreditUtilisation: Int,
    val changeInShortTermDebt: Int,
    val currentLongTermDebt: Int,
    val currentLongTermNonPromotionalDebt: Int,
    val currentLongTermCreditLimit: Int?,
    val currentLongTermCreditUtilisation: Int?,
    val changeInLongTermDebt: Int,
    val numPositiveScoreFactors: Int,
    val numNegativeScoreFactors: Int,
    val equifaxScoreBand: Int,
    val equifaxScoreBandDescription: String,
    val daysUntilNextReport: Int,
) {
    companion object {
        fun default() = CreditReportInfoJson(
            score = 0,
            scoreBand = 0,
            clientRef = "",
            status = "",
            maxScoreValue = 0,
            minScoreValue = 0,
            monthsSinceLastDefaulted = 0,
            hasEverDefaulted = false,
            monthsSinceLastDelinquent = 0,
            hasEverBeenDelinquent = false,
            percentageCreditUsed = 0,
            percentageCreditUsedDirectionFlag = 0,
            changedScore = 0,
            currentShortTermDebt = 0,
            currentShortTermNonPromotionalDebt = 0,
            currentShortTermCreditLimit = 0,
            currentShortTermCreditUtilisation = 0,
            changeInShortTermDebt = 0,
            currentLongTermDebt = 0,
            currentLongTermNonPromotionalDebt = 0,
            currentLongTermCreditLimit = 0,
            currentLongTermCreditUtilisation = 0,
            changeInLongTermDebt = 0,
            numPositiveScoreFactors = 0,
            numNegativeScoreFactors = 0,
            equifaxScoreBand = 0,
            equifaxScoreBandDescription = "",
            daysUntilNextReport = 0
        )
    }
}