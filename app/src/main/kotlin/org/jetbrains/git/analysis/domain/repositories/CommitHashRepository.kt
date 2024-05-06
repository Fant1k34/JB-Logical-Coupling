package org.jetbrains.git.analysis.domain.repositories

interface CommitHashRepository {
    fun getListCommitHash(repositoryName: String, ownerName: String, token: String): List<String>
}
