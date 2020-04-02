package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class JacocoTool(val projectRoot: File) {

    private val log = LoggerFactory.getLogger(JacocoTool::class.java)

    fun execToSql(): File? {
        prepareJar()
        call(listOf("java", "-jar", "scan_jacoco.jar", "--target-project=${projectRoot.absolutePath}"))
        val sqlFile = File("${projectRoot.absolutePath}/jacoco.sql")
        return if (sqlFile.exists()) {
            sqlFile
        } else {
            log.info("failed to get jacoco.sql")
            null
        }
    }

    private fun prepareJar() {
        val jarLink = "http://ci.archguard.org/job/code-scanners/lastSuccessfulBuild/artifact/scan_jacoco/target/scan_jacoco-1.0-SNAPSHOT-jar-with-dependencies.jar"
        FileOperator.download(URL(jarLink), File(projectRoot.absolutePath + "/scan_jacoco.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_jacoco.jar")
        chmod.directory(projectRoot)
        chmod.start().waitFor()
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }
}