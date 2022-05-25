package com.thoughtworks.archguard.smartscanner

import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.archguard.scanner.core.context.AnalyserType
import java.io.File

data class ScannerCommand(
    val type: AnalyserType,
    val systemId: String,
    val serverUrl: String,
    val path: String,
    // for archguard usage
    val workspace: File,
    val logStream: StreamConsumer,
    val branch: String,
) {
    fun toArguments(): List<String> {
        val arguments = mutableListOf(
            "--type=${type.name.lowercase()}",
            "--system-id=$systemId",
            "--server-url=$serverUrl",
            "--path=${workspace.resolve(path).absolutePath}",
            "--workspace=${workspace.absolutePath}",
            "--workspace=$branch",
        )
        // additional args
        outputs.forEach { arguments.add("--output=$it") }
        language?.let { arguments.add("--language=$it") }
        features.forEach { arguments.add("--features=$it") }
        repoId?.let { arguments.add("--repo-id=$it") }
        additionArguments.forEach(arguments::add)
        return arguments
    }

    // TODO configurable output format
    var outputs = listOf("http")

    // for source code analysing, may be Map, String or AnalyserSpec
    // TODO support official analyser only, accept config and json to enable customized analyser
    var language: String? = null
    var features: List<String> = listOf()
    var repoId: String? = null
    var additionArguments: List<String> = emptyList()
}