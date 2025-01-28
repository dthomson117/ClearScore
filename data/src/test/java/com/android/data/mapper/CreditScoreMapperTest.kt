package com.android.data.mapper

import com.android.data.model.CreditReportInfoJson
import com.android.data.model.CreditScoreJson
import com.android.domain.model.CreditReportInfo
import com.android.domain.model.CreditScore
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CreditScoreMapperTest {
    private val creditReportInfoMapper = CreditReportInfoMapper()
    private lateinit var sut: CreditScoreMapper

    @Before
    fun setUp() {
        sut = CreditScoreMapper(creditReportInfoMapper = creditReportInfoMapper)
    }

    @Test
    fun `toDomain SHOULD return CreditScore WHEN CreditScoreJson is provided`() {
        val result = sut.toDomain(creditScoreJson)

        expectThat(result).isEqualTo(creditScore)
    }

    private val creditReportInfo = CreditReportInfo(
        score = 300,
        maxScoreValue = 700,
        minScoreValue = 0
    )

    private val creditReportInfoJson = CreditReportInfoJson(
        score = 300,
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

    private val creditScore = CreditScore(
        creditReportInfo = creditReportInfo
    )

    private val creditScoreJson = CreditScoreJson.default().copy(
        creditReportInfo = creditReportInfoJson
    )
}