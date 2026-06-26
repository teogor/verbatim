package dev.teogor.verbatim.core

import java.util.logging.Level
import java.util.logging.Logger

/**
 * JVM implementation of platform utilities.
 * Uses `java.util.logging.Logger` for native logging output.
 */
actual object Platform {
    private val logger: Logger = Logger.getLogger("Verbatim")

    actual fun getenv(name: String): String? = System.getenv(name)

    actual fun getProperty(name: String): String? = System.getProperty(name)

    actual fun currentThreadName(): String = Thread.currentThread().name ?: "unknown"

    actual fun platformLog(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val julLevel = when (level) {
            LogLevel.VERBOSE,
            LogLevel.DEBUG -> Level.FINE
            LogLevel.INFO -> Level.INFO
            LogLevel.WARN -> Level.WARNING
            LogLevel.ERROR -> Level.SEVERE
            LogLevel.FATAL -> Level.SEVERE
            LogLevel.OFF -> return
        }

        val record = java.util.logging.LogRecord(julLevel, "[$tag] $message")
        record.loggerName = logger.name
        record.sourceClassName = tag
        if (throwable != null) {
            record.thrown = throwable
        }
        logger.log(record)
    }
}
