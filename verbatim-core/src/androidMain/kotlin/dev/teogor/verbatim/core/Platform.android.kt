package dev.teogor.verbatim.core

import android.util.Log

/**
 * Android implementation of platform utilities.
 * Uses `android.util.Log` for native Logcat output.
 */
actual object Platform {
    actual fun getenv(name: String): String? = System.getenv(name)

    actual fun getProperty(name: String): String? = System.getProperty(name)

    actual fun currentThreadName(): String = Thread.currentThread().name ?: "unknown"

    actual fun platformLog(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        val logMessage = if (throwable != null) {
            "$message\n${throwable.stackTraceToString()}"
        } else {
            message
        }

        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, logMessage)
            LogLevel.DEBUG -> Log.d(tag, logMessage)
            LogLevel.INFO -> Log.i(tag, logMessage)
            LogLevel.WARN -> Log.w(tag, logMessage)
            LogLevel.ERROR -> Log.e(tag, logMessage)
            LogLevel.FATAL -> Log.e(tag, "FATAL: $logMessage")
            LogLevel.OFF -> {}
        }
    }
}
