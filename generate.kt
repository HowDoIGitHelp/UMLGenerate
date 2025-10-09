package umlGenerate

import java.io.File
import java.io.FileNotFoundException
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

class ProcessCompletionException(message: String) : Exception(message)

fun pathExists(pathString: String) = File(pathString).exists()

fun completeProcess(process: ProcessBuilder, completionMessage: String = "completed process") {
    val startedProcess = process.redirectErrorStream(true).start()
    val output = startedProcess.inputStream.bufferedReader().readText()
    when (val exitCode = startedProcess.waitFor()) {
        0 -> println(completionMessage)
        else -> throw ProcessCompletionException("Process did not complete with exit code:$exitCode\ncommand:${process.command().joinToString(" ")}\noutput:$output")
    }
}

fun main() {
    val umlInputs = File("umlInputs.in").readText().split("\n\n")

    umlInputs.forEach {
        val splitInput = it.split("\n")
        val umlOutputFileName = splitInput.last()
        val sourceFile = splitInput.first()

        if (!pathExists(sourceFile))
            throw FileNotFoundException("path $sourceFile not found!")

        val buildProcess = ProcessBuilder("kotlinc", "uml.kt", splitInput.first(), "-include-runtime", "-d", "uml.jar")
            .apply { environment().remove("KOTLIN_RUNNER") }
        val runProcess = ProcessBuilder("java", "-cp", "uml.jar", "umlGenerate.UmlKt", *splitInput.drop(1).toTypedArray())
        val generateDiagramProcess = ProcessBuilder("java", "-jar", "plantuml-1.2025.7.jar", "-tsvg", umlOutputFileName)

        try {
            completeProcess(buildProcess, "building uml.jar complete")
            completeProcess(runProcess, "generated $umlOutputFileName")
            completeProcess(generateDiagramProcess, "generated diagram for $umlOutputFileName")
            println()
        } catch (e: ProcessCompletionException) {
            println(e.message)
        } finally {
            Path("uml.jar").deleteIfExists()
        }
    }
}

fun debugAppEnvironment() {
    println("=== APPLICATION ENVIRONMENT ===")
    val env = System.getenv()
    env.keys.sorted().forEach { key ->
        if (key.contains("KOTLIN", ignoreCase = true)) {
            println("$key=${env[key]}")
        }
    }

    // Check if KOTLIN_RUNNER is set in the app
    println("KOTLIN_RUNNER in app: ${System.getenv("KOTLIN_RUNNER")}")
}