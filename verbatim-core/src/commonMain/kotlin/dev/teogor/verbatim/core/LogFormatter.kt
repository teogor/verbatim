package dev.teogor.verbatim.core

/**
 * A functional interface for formatting log events into strings.
 *
 * Implementations of this interface convert [LogEvent] objects into formatted strings
 * suitable for output to various destinations (console, file, remote service, etc.).
 *
 * Example usage:
 * ```kotlin
 * val formatter = LogFormatter { event ->
 *     "[${event.level}] ${event.loggerName}: ${event.message}"
 * }
 * ```
 */
fun interface LogFormatter {
    /**
     * Format a log event into a string.
     *
     * @param event The log event to format.
     * @return The formatted string.
     */
    fun format(event: LogEvent): String
}
