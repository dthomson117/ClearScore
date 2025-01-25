package com.android.data.mapper

import com.android.data.model.CreditReportInfoJson
import com.android.domain.model.CreditReportInfo
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CreditReportInfoMapperTest {
    private lateinit var sut: CreditReportInfoMapper

    @Before
    fun setUp() {
        sut = CreditReportInfoMapper()
    }

    @Test
    fun `toDomain SHOULD return CreditReportInfo WHEN CreditReportInfoJson is provided`() {
        val result = sut.toDomain(creditReportInfoJson)

        expectThat(result).isEqualTo(creditReportInfo)
    }

    private val creditReportInfo = CreditReportInfo(
        score = 300,
        maxScoreValue = 700,
        minScoreValue = 0
    )

    private val creditReportInfoJson = CreditReportInfoJson(
        score= 300,
        scoreBand = 4,
        clientRef = "CS-SED-655426-708782",
        status = "MATCH",
        maxScoreValue = 700,
        minScoreValue = 0,
        monthsSinceLastDefaulted = -1,
        hasEverDefaulted = false,
        monthsSinceLastDelinquent = 1,
        hasEverBeenDelinquent = true,
        percentageCreditUsed = 44,
        percentageCreditUsedDirectionFlag = 1,
        changedScore = 0,
        currentShortTermDebt = 13758,
        currentShortTermNonPromotionalDebt = 13758,
        currentShortTermCreditLimit = 30600,
        currentShortTermCreditUtilisation = 44,
        changeInShortTermDebt = 549,
        currentLongTermDebt = 24682,
        currentLongTermNonPromotionalDebt = 24682,
        currentLongTermCreditLimit = null,
        currentLongTermCreditUtilisation = null,
        changeInLongTermDebt = -327,
        numPositiveScoreFactors = 9,
        numNegativeScoreFactors = 0,
        equifaxScoreBand = 4,
        equifaxScoreBandDescription = "Excellent",
        daysUntilNextReport = 9
    )
}