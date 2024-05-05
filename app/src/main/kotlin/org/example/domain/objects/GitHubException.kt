package domain.objects

class GitHubException(override val message: String): RuntimeException(message)
