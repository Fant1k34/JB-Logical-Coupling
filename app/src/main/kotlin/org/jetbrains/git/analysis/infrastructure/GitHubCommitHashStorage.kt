package org.jetbrains.git.analysis.infrastructure

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.git.analysis.domain.objects.GitHubException
import org.jetbrains.git.analysis.domain.objects.ParseException
import org.jetbrains.git.analysis.domain.repositories.CommitHashRepository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GitHubCommitHashStorage : CommitHashRepository {
    override fun getListCommitHash(repositoryName: String, ownerName: String, token: String): List<String> {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/$ownerName/$repositoryName/commits"))
            .header("Accept", "application/vnd.github+json")
            .header("Authorization", "Bearer $token")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) throw GitHubException(
            "Repository and owner within this access token does not exist"
        )

        return getListCommitHashFromResponse(response.body())
    }

    private fun getListCommitHashFromResponse(line: String): List<String> {
        try {
            val type = object : TypeToken<List<Map<String, Any>>>() {}.type
            val result: List<Map<String, Any>> = Gson().fromJson(line, type)

            return result.map {
                it["sha"] as String
            }.toList()
        } catch (_: Throwable) {
            throw ParseException("Mapping exception while parsing Hash Commit")
        }
    }
}
