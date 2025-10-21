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
    val umlInputs = File("umlInputs.in")
        .readText()
        .trimEnd()
        .split("\n\n")

    val umlOutputDirectory = umlSources.umlOutputDir
    for (umlInput in umlSources.diagrams) {
        // val splitInput = umlInput.split("\n")
        val sourceFiles = umlInput.sources.map { it.filename }
        val qualifiedClassNames = umlInput.packages.map { pkg ->
            pkg.includedClasses.map {
                "${pkg.name}.${it.name}"
            }
        }.fold(listOf<String>()) { l1,l2 ->
            l1 + l2
        }

        val umlOutputFileName = "${umlInput.umlOutputDir}${umlInput.umlOutput}"
        val programArgs = qualifiedClassNames + listOf(umlOutputFileName)

        umlInput.packages.forEach {
            println(it.name)
            it.includedClasses.forEach {
                println(it.name)
            }
        }

        println(sourceFiles)
        println(programArgs)

        sourceFiles.forEach {
            if (!pathExists(it))
                throw FileNotFoundException("path $it not found!")
        }

        val buildProcess = ProcessBuilder("kotlinc", "uml.kt", *sourceFiles.toTypedArray(), "-include-runtime", "-d", "uml.jar")
            .apply { environment().remove("KOTLIN_RUNNER") }
        val runProcess = ProcessBuilder("java", "-cp", "uml.jar", "umlGenerate.UmlKt", *programArgs.toTypedArray())
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
    println("APPLICATION ENVIRONMENT")
    val env = System.getenv()
    env.keys.sorted().forEach { key ->
        if (key.contains("KOTLIN", ignoreCase = true)) {
            println("$key=${env[key]}")
        }
    }

    // Check if KOTLIN_RUNNER is set in the app
    println("KOTLIN_RUNNER in app: ${System.getenv("KOTLIN_RUNNER")}")
}