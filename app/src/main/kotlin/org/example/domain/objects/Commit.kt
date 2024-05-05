package domain.objects

data class Commit(val hash: String, val author: Contributor, val files: List<File>)