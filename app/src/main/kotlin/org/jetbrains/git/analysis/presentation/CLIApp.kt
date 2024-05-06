package org.jetbrains.git.analysis.presentation

import org.jetbrains.git.analysis.application.ApplicationConfiguration

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

        val application = ApplicationConfiguration(repository, owner, token)
        try {
            application.execute().forEach {
                println("- File ${it.key}")
                println("  ${it.value.joinToString("; ")}")
            }
        } catch (e: Throwable) {
            println("Unfortunately, error occurred. Please, read it's message below")
            println(e.message)
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
