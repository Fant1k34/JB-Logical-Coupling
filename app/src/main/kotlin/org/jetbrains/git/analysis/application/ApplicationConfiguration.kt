package org.jetbrains.git.analysis.application

import org.jetbrains.git.analysis.domain.servicies.PairAnalyserService
import org.jetbrains.git.analysis.infrastructure.GitHubCommitHashStorage
import org.jetbrains.git.analysis.infrastructure.GitHubCommitInfoStorage

class ApplicationConfiguration(
    private val repositoryName: String,
    private val ownerName: String,
    private val token: String
) {
    fun execute(): Map<String, List<String>> {
        val commitHashStorage = GitHubCommitHashStorage()
        val commitInfoStorage = GitHubCommitInfoStorage()

        val analyserService =
            PairAnalyserService(repositoryName, ownerName, token, commitHashStorage, commitInfoStorage)

        return analyserService.analise()
    }
}
