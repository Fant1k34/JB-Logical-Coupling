package org.jetbrains.git.analysis.domain.servicies

import org.example.domain.objects.*
import org.jetbrains.git.analysis.infrastructure.GitHubCommitHashStorage
import org.jetbrains.git.analysis.infrastructure.GitHubCommitInfoStorage
import org.jetbrains.git.analysis.domain.objects.Contributor

class PairAnalyserService(
    private val repositoryName: String,
    private val ownerName: String,
    private val token: String,
    private val commitHashStorage: GitHubCommitHashStorage,
    private val commitInfoStorage: GitHubCommitInfoStorage
) {
    fun analise(): Map<String, List<String>> {
        val fileStatistics = mutableMapOf<String, MutableMap<Contributor, Int>>()

        val commitHashList = commitHashStorage.getListCommitHash(repositoryName, ownerName, token)

        commitHashList.map {
            commitInfoStorage.getCommitInfo(repositoryName, ownerName, token, it)
        }.forEach { commit ->
            commit.files.forEach { file ->
                if (fileStatistics[file.filename] == null) {
                    fileStatistics[file.filename] = mutableMapOf()
                }

                val contributorsMap = fileStatistics[file.filename]

                if (contributorsMap?.get(commit.author) == null) {
                    contributorsMap?.set(commit.author, 0)
                }

                contributorsMap?.set(commit.author, file.changes)
            }
        }

        return fileStatistics.map {
            val filename = it.key
            val contributors = it.value

            filename to chooseContributorPair(contributors)
        }.toMap()
    }

    private fun chooseContributorPair(contributorMap: MutableMap<Contributor, Int>): List<String> =
        contributorMap.toList()
            .sortedBy { (_, value) -> -value }
            .take(2)
            .map {
                "Contributor ${it.first.name}, ${it.first.email}"
            }
}
