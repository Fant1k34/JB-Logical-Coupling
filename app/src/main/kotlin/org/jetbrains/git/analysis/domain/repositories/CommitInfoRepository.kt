package org.jetbrains.git.analysis.domain.repositories

import org.jetbrains.git.analysis.domain.objects.Commit

interface CommitInfoRepository {
    fun getCommitInfo(
        repositoryName: String,
        ownerName: String,
        token: String,
        commitHash: String
    ): Commit
}
