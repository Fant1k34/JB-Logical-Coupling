package domain.servicies

import domain.objects.Commit
import domain.objects.GitHubException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import domain.objects.Contributor
import domain.objects.File

class CommitService(private val repositoryName: String, private val ownerName: String, private val token: String) {
    private fun getListCommitHashFromResponse(line: String): List<String> {
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val result: List<Map<String, Any>> = Gson().fromJson(line, type)

        return result.map {
            it["sha"] as String
        }.toList()
    }

    private fun getCommitFromResponse(line: String): Commit {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val result: Map<String, Any> = Gson().fromJson(line, type)

        return mapStringToCommit(result)
    }

    private fun castChangesToInt(value: Double): Int = Math.round(value).toInt()

    private fun mapFileJsonToFile(fileJson: Map<*, *>): File {
        val filename = fileJson["filename"] as String
        val additions = fileJson["additions"] as Double
        val deletions = fileJson["deletions"] as Double
        val changes = fileJson["changes"] as Double

        return File(
            filename,
            castChangesToInt(additions),
            castChangesToInt(deletions),
            castChangesToInt(changes)
        )
    }


    private fun mapStringToCommit(line: Map<String, Any>): Commit {
        try {
            val hash = line["sha"] as String

            val name = ((line["commit"] as Map<*, *>)["author"] as Map<*, *>)["name"] as String
            val email = ((line["commit"] as Map<*, *>)["author"] as Map<*, *>)["email"] as String

            val author = Contributor(name, email)

            val filesList = (line["files"] as List<*>).map {
                mapFileJsonToFile(it as Map<*, *>)
            }

            return Commit(hash, author, filesList)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw GitHubException("Mapping exception")
        }
    }

    fun getCommitByCommitHash(commitHash: String): Commit {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/$ownerName/$repositoryName/commits/$commitHash"))
            .header("Accept", "application/vnd.github+json")
            .header("Authorization", "Bearer $token")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) throw GitHubException("Repository and owner within this access token does not exist")

        return getCommitFromResponse(response.body())
    }


    fun getListCommitHash(): List<String> {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/$ownerName/$repositoryName/commits"))
            .header("Accept", "application/vnd.github+json")
            .header("Authorization", "Bearer $token")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) throw GitHubException("Repository and owner within this access token does not exist")

        return getListCommitHashFromResponse(response.body())
    }
}