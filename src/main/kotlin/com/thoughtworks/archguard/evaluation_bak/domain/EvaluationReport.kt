package com.thoughtworks.archguard.evaluation_bak.domain

import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.ReportDms
import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.ReportLevel
import java.io.Serializable
import java.time.LocalDateTime

data class EvaluationReport(val id: String?,
                            val createdDate: LocalDateTime,
                            val name: String,
                            val dimensions: List<Dimension>,
                            val comment: String,
                            val improvements: List<String>)

data class Dimension(val name: String, val reportDms: Map<ReportDms, ReportLevel>) : Serializable {
    constructor() : this("", LinkedHashMap())
}