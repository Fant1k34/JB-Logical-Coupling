package domain.objects

data class File(val filename: String, val additions: Int, val deletions: Int, val changes: Int)
