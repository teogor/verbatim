package dev.teogor.verbatim.core

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.Foundation.NSLog

/**
 * Apple/Native implementation of platform utilities.
 * Covers iOS, macOS, tvOS, and watchOS.
 * Uses `NSLog` for native logging output.
 */
actual object Platform {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getenv(name: String): String? = platform.posix.getenv(name)?.toKString()

    actual fun getProperty(name: String): String? = null

    actual fun currentThreadName(): String = "main"

    actual fun platformLog(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val logMessage = if (throwable != null) {
            "$message\n${throwable.stackTraceToString()}"
        } else {
            message
        }

        when (level) {
            LogLevel.VERBOSE,
            LogLevel.DEBUG -> NSLog("[%s] %s: %s", "DEBUG", tag, logMessage)
            LogLevel.INFO -> NSLog("[%s] %s: %s", "INFO", tag, logMessage)
            LogLevel.WARN -> NSLog("[%s] %s: %s", "WARN", tag, logMessage)
            LogLevel.ERROR -> NSLog("[%s] %s: %s", "ERROR", tag, logMessage)
            LogLevel.FATAL -> NSLog("[%s] %s: FATAL: %s", "ERROR", tag, logMessage)
            LogLevel.OFF -> {}
        }
    }
}
