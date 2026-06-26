package dev.teogor.verbatim.core

import kotlin.time.Instant

/**
 * Represents a single log event captured at a point in time.
 *
 * @property level The severity level of the event.
 * @property loggerName The name of the logger that created the event.
 * @property message The log message.
 * @property throwable An optional exception associated with the event.
 * @property attributes Structured key-value pairs attached at the call site.
 * @property context The ambient [LogContext] active at the time the event was created.
 * @property thread The name of the thread on which the event was created.
 * @property timestamp The time at which the event was created.
 */
data class LogEvent(
    val level: LogLevel,
    val loggerName: String,
    val message: String?,
    val throwable: Throwable?,
    val attributes: Map<String, Any?>,
    val context: LogContext,
    val thread: String,
    val timestamp: Instant
)
