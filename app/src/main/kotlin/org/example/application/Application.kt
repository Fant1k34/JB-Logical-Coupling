package application

import domain.objects.Contributor
import domain.servicies.CommitService

class Application(private val repositoryName: String, private val ownerName: String, private val token: String) {
    fun analise(): Map<String, List<String>> {
        val service = CommitService(repositoryName, ownerName, token)

        val fileStatistics = mutableMapOf<String, MutableMap<Contributor, Int>>()

        service.getListCommitHash().map {
            service.getCommitByCommitHash(it)
        }.map { commit ->
            commit.files.forEach { file ->
                if (fileStatistics.get(file.filename) == null) {
                    fileStatistics[file.filename] = mutableMapOf()
                }

                val contributorsMap = fileStatistics[file.filename]

                if (contributorsMap?.get(commit.author) == null) {
                    contributorsMap?.set(commit.author, 0)
                }

                contributorsMap?.set(commit.author, file.changes)
            }
        }

        val bestFileContributors = mutableMapOf<String, List<String>>()

        fileStatistics.forEach {
            val filename = it.key

            val contributors = it.value.toList()
                .sortedBy { (key, value) -> value }
                .take(2)
                .map {
                    "Contributor ${it.first.name}, ${it.first.email}"
                }

            bestFileContributors[filename] = contributors
        }

        return bestFileContributors
    }
}