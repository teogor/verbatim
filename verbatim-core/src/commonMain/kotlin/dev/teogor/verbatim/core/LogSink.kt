package dev.teogor.verbatim.core

/**
 * Interface representing a destination for log events.
 */
interface LogSink {
    /**
     * Emits a log event to the output destination.
     */
    fun emit(event: LogEvent)

    /**
     * Flushes any buffered log events.
     */
    fun flush() {}
}
