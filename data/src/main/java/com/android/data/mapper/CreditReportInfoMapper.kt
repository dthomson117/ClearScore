package com.android.data.mapper

import com.android.data.model.CreditReportInfoJson
import com.android.domain.model.CreditReportInfo

/**
 * Mapper class used to transform [CreditReportInfoJson] into [CreditReportInfo].
 */
class CreditReportInfoMapper {
    fun toDomain(creditReportInfoJson: CreditReportInfoJson): CreditReportInfo {
        return CreditReportInfo(
            score = creditReportInfoJson.score,
            maxScoreValue = creditReportInfoJson.maxScoreValue,
            minScoreValue = creditReportInfoJson.minScoreValue
        )
    }
}