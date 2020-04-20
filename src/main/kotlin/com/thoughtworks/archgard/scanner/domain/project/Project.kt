package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.eclipse.jgit.api.Git
import org.slf4j.LoggerFactory
import java.io.File

class Project(val id: String, val projectName: String, val gitRepo: String) {
    private val log = LoggerFactory.getLogger(Project::class.java)

    fun build(): CompiledProject {
        val workspace = createTempDir()
        log.info("workspace is: {}, gitRepo is: {}", workspace.toPath().toString(), gitRepo)
        getSource(workspace, this.gitRepo)
        buildSource(workspace)
        return CompiledProject(gitRepo, workspace)
    }

    fun getSource(): CompiledProject {
        val workspace = createTempDir()
        log.info("workspace is: {}, gitRepo is: {}", workspace.toPath().toString(), gitRepo)
        getSource(workspace, this.gitRepo)
        return CompiledProject(gitRepo, workspace)
    }

    private fun getSource(workspace: File, repo: String) {
        Git.cloneRepository()
                .setDirectory(workspace)
                .setURI(repo)
                .call()
    }

    private fun buildSource(workspace: File) {
        val pb = if (workspace.listFiles().orEmpty().any { it.name == "pom.xml" }) {
            ProcessBuilder("./mvnw", "clean", "package", "-Dmaven.test.failure.ignore=true")
        } else {
            ProcessBuilder("./gradlew", "--continue","clean", "build")
        }
        Processor.executeWithLogs(pb, workspace)
    }

}