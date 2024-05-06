# Repository Contributors Analysis

## Description

This project provides a console application to identify the pairs of developers who
have made the most changes to the project file.

For the application to work, you must enter the following information into the input console:

1. Repository name (name of the repository to be analyzed)
2. Owner of repository (name of the repository creator)
3. Your GitHub token

## Principle of analysis

The main principle is the idea of analyzing each commit in the master branch. For each commit, there is a list
of modified files. Then, the analysis remembers the number of changes in each of the files for this commit author. As a
result, a list of all contributors to the current file is determined for each file. After that,
the two most active contributors are selected for each file based on the number of total changes for adding lines and
for deleting

### Advantages of the analysis

1. It takes into account all kinds of files throughout the commit history, not just a slice at the current time
2. It makes it easy to customize and identify not pairs, but N developers who have made a great contribution to the
   project
3. Allows you to easily replace the formula for calculating the contribution to file editing, since it takes into
   account both the number of the number of added and deleted rows
4. Error handling during parsing and receiving information via GitHubAPI

### Disadvantages of the analysis

1. Does not take into account renamed files
2. Determines the uniqueness of the user by First and Last Name, as well as by email. If the
   email and/or name are replaced, the analysis will perceive the developer as a new contributor

## How to run?

Environment:

1. JVM 21+
2. Kotlin Compiler
3. Gradle 8.7

Start Console Application:
```./gradlew build && ./gradlew bootRun```

## Screenshots


