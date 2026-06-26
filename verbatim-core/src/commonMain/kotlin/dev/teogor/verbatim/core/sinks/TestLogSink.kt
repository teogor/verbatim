package dev.teogor.verbatim.core.sinks

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LogSink

/**
 * Test log sink for unit testing.
 *
 * This sink captures log events in memory for verification in tests.
 *
 * Example usage:
 * ```kotlin
 * val testSink = TestLogSink()
 * VerbatimPipeline.addSink(testSink)
 *
 * logger.info { "Test message" }
 *
 * assertEquals(1, testSink.events.size)
 * assertEquals("Test message", testSink.lastMessage())
 * ```
 */
class TestLogSink : LogSink {
    private val _events = mutableListOf<LogEvent>()
    
    /**
     * All captured log events.
     */
    val events: List<LogEvent> get() = _events.toList()

    /**
     * Emit a log event by capturing it in the internal list.
     *
     * @param event The log event to capture.
     */
    override fun emit(event: LogEvent) {
        _events.add(event)
    }

    /**
     * Flush any buffered log events.
     */
    override fun flush() {
        // No buffering in test implementation
    }

    /**
     * Clear all captured events.
     */
    fun clear() {
        _events.clear()
    }

    /**
     * Check if any event has the specified level.
     *
     * @param level The log level to check for.
     * @return true if at least one event has the specified level.
     */
    fun hasLevel(level: LogLevel): Boolean = events.any { it.level == level }

    /**
     * Get all messages with the specified level.
     *
     * @param level The log level to filter by.
     * @return List of messages with the specified level.
     */
    fun messagesWithLevel(level: LogLevel): List<String> = 
        events.filter { it.level == level }.mapNotNull { it.message }

    /**
     * Get the last captured event.
     *
     * @return The last event, or null if no events have been captured.
     */
    fun lastEvent(): LogEvent? = events.lastOrNull()

    /**
     * Get the message of the last captured event.
     *
     * @return The last message, or null if no events have been captured.
     */
    fun lastMessage(): String? = lastEvent()?.message

    /**
     * Get the number of captured events.
     */
    val size: Int get() = events.size
}
