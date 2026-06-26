package dev.teogor.verbatim.core.visuals

import dev.teogor.verbatim.core.LogLevel

/**
 * Functional interface for converting log levels to visual indicators.
 *
 * Implementations of this interface map a [LogLevel] to a visual string representation
 * (e.g., emoji, icon, color code).
 *
 * Example usage:
 * ```kotlin
 * val visual = LogLevelVisual { level ->
 *     when (level) {
 *         LogLevel.VERBOSE -> "💜"
 *         LogLevel.DEBUG -> "💚"
 *         LogLevel.INFO -> "💙"
 *         LogLevel.WARN -> "💛"
 *         LogLevel.ERROR -> "❤️"
 *         LogLevel.FATAL -> "💔"
 *         LogLevel.OFF -> ""
 *     }
 * }
 * ```
 */
fun interface LogLevelVisual {
    /**
     * Get the visual indicator for a log level.
     *
     * @param level The log level.
     * @return The visual indicator string.
     */
    fun getIndicator(level: LogLevel): String
}
