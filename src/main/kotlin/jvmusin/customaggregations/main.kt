package jvmusin.customaggregations

import java.io.File

/**
 * Main method.
 *
 * Reads the file provided in a first cmd argument,
 * prepends its content with its table of contents and prints it to `stdout`.
 */
fun main(args: Array<String>) {
    try {
        val appender = ContentsPrepender()
        val file = File(args[0])
        val text = file.readText()
        println(appender.prependWithContents(text))
    } catch (e: Exception) {
        System.err.println("Unable to prepend the file with its content table: ${e.message}")
        e.printStackTrace()
    }
}
