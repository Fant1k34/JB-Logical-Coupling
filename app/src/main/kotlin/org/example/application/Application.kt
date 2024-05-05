package application

import domain.servicies.CommitService

class Application(private val repositoryName: String, private val ownerName: String, private val token: String) {
    fun analise(): Map<String, List<String>> {
        val service = CommitService(repositoryName, ownerName, token)

        service.getListCommitHash().map {
            service.getCommitByCommitHash(it)
        }.forEach {
            println(it)
        }

        return emptyMap()
    }
}