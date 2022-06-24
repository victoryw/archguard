package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto

interface InsightRepository {
    abstract fun filterByCondition(id: Long): List<ScaModelDto>
    abstract fun saveInsight(insight: CustomInsight): Long
    abstract fun getInsightByName(name: String): CustomInsight
}
