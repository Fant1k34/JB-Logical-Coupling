package presentation

import application.Application

class CLIApp {
    fun run() {
        println("Repository Contributors Analysis for GitHub")
        val repository = getDataFromUser(
            "Repository name:",
            "Please, provide repository name:"
        )
        val owner = getDataFromUser(
            "Owner of repository:",
            "Please, provide owner of repository:"
        )
        val token = getDataFromUser(
            "Your GitHub token:",
            "Please, provide your GitHub token:"
        )

        val application = Application(repository, owner, token)
        application.analise().forEach{
            println("- File ${it.key}")
            println("  ${it.value.joinToString("; ")}")
        }
    }

    private fun getDataFromUser(queryText: String, queryRepeatText: String): String {
        println(queryText)
        var input = readlnOrNull() ?: throw RuntimeException("User interrupted input")

        while (input == "" || input.split(" ").size != 1) {
            println(queryRepeatText)
            input = readlnOrNull() ?: throw RuntimeException("User interrupted input")
        }
        return input
    }
}