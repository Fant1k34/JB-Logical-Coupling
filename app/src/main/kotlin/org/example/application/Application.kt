package org.example.application

import org.example.domain.objects.Contributor
import org.example.domain.servicies.CommitService


class Application(repositoryName: String, ownerName: String, token: String) {
    private val service = CommitService(repositoryName, ownerName, token)

    private fun chooseContributorPair(contributorMap: MutableMap<Contributor, Int>): List<String> =
        contributorMap.toList()
            .sortedBy { (_, value) -> -value }
            .take(2)
            .map {
                "Contributor ${it.first.name}, ${it.first.email}"
            }

    fun analise(): Map<String, List<String>> {
        val fileStatistics = mutableMapOf<String, MutableMap<Contributor, Int>>()

        service.getListCommitHash().map {
            service.getCommitByCommitHash(it)
        }.map { commit ->
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
}
