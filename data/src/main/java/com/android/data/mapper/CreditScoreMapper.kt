package com.android.data.mapper

import com.android.data.model.CreditReportInfoJson
import com.android.data.model.CreditScoreJson
import com.android.domain.model.CreditReportInfo
import com.android.domain.model.CreditScore

/**
 * Mapper class used to transform [CreditReportInfoJson] into [CreditReportInfo].
 */
class CreditScoreMapper(
    private val creditReportInfoMapper: CreditReportInfoMapper,
) {
    fun toDomain(creditScoreJson: CreditScoreJson): CreditScore {
        return CreditScore(
            creditReportInfo = creditReportInfoMapper.toDomain(creditScoreJson.creditReportInfo)
        )
    }
}