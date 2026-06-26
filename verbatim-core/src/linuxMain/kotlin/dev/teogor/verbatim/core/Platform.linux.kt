package dev.teogor.verbatim.core

/**
 * Linux implementation of platform utilities.
 * Uses `println` (stdout) for logging output.
 */
actual object Platform {
    actual fun getenv(name: String): String? = null

    actual fun getProperty(name: String): String? = null

    actual fun currentThreadName(): String = "main"

    actual fun platformLog(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val prefix = when (level) {
            LogLevel.VERBOSE -> "V"
            LogLevel.DEBUG -> "D"
            LogLevel.INFO -> "I"
            LogLevel.WARN -> "W"
            LogLevel.ERROR -> "E"
            LogLevel.FATAL -> "F"
            LogLevel.OFF -> return
        }

        println("$prefix/$tag: $message")
        if (throwable != null) {
            println(throwable.stackTraceToString())
        }
    }
}
