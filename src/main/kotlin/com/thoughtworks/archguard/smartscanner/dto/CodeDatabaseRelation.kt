package com.thoughtworks.archguard.smartscanner.dto

data class CodeDatabaseRelation(
    val packageName: String = "",
    val className: String = "",
    val functionName: String = "",
    val tables: List<String> = listOf(),
    val sqls: List<String> = listOf()
)