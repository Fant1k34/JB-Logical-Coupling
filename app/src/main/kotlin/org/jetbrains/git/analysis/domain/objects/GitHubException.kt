package org.jetbrains.git.analysis.domain.objects

class GitHubException(override val message: String): RuntimeException(message)
