package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.formatters.CompactLogFormatter
import dev.teogor.verbatim.core.formatters.DefaultLogFormatter
import dev.teogor.verbatim.core.formatters.JsonLogFormatter
import dev.teogor.verbatim.core.formatters.PrettyLogFormatter
import dev.teogor.verbatim.core.visuals.LogVisuals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogFormatterTest {

    private fun createTestEvent(
        level: LogLevel = LogLevel.INFO,
        loggerName: String = "TestLogger",
        message: String = "Test message",
        throwable: Throwable? = null,
        attributes: Map<String, Any?> = emptyMap()
    ): LogEvent {
        return LogEvent(
            level = level,
            loggerName = loggerName,
            message = message,
            throwable = throwable,
            attributes = attributes,
            context = LogContext(),
            thread = "main",
            timestamp = kotlin.time.Instant.fromEpochMilliseconds(1705312245123)
        )
    }

    @Test
    fun testDefaultFormatter() {
        val formatter = DefaultLogFormatter(LogVisuals.Minimalist)
        val event = createTestEvent()
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("[I]"))
        assertTrue(formatted.contains("TestLogger"))
        assertTrue(formatted.contains("Test message"))
    }

    @Test
    fun testDefaultFormatterWithEmoji() {
        val formatter = DefaultLogFormatter(LogVisuals.Emojis)
        val event = createTestEvent()
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("💙"))
        assertTrue(formatted.contains("INFO"))
    }

    @Test
    fun testDefaultFormatterWithThrowable() {
        val formatter = DefaultLogFormatter()
        val throwable = RuntimeException("Test exception")
        val event = createTestEvent(throwable = throwable)
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("Exception: Test exception"))
    }

    @Test
    fun testDefaultFormatterWithAttributes() {
        val formatter = DefaultLogFormatter()
        val event = createTestEvent(attributes = mapOf("key" to "value"))
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("Attributes:"))
        assertTrue(formatted.contains("key=value"))
    }

    @Test
    fun testCompactFormatter() {
        val formatter = CompactLogFormatter(LogVisuals.Minimalist)
        val event = createTestEvent()
        val formatted = formatter.format(event)

        assertEquals("[I] TestLogger: Test message", formatted)
    }

    @Test
    fun testPrettyFormatter() {
        val formatter = PrettyLogFormatter(LogVisuals.Minimalist)
        val event = createTestEvent()
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("[I] TestLogger"))
        assertTrue(formatted.contains("main"))
        assertTrue(formatted.contains("Test message"))
    }

    @Test
    fun testPrettyFormatterWithAttributes() {
        val formatter = PrettyLogFormatter()
        val event = createTestEvent(attributes = mapOf("key" to "value"))
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("Attributes:"))
        assertTrue(formatted.contains("key=value"))
    }

    @Test
    fun testJsonFormatter() {
        val formatter = JsonLogFormatter()
        val event = createTestEvent()
        val formatted = formatter.format(event)

        assertTrue(formatted.startsWith("{"))
        assertTrue(formatted.endsWith("}"))
        assertTrue(formatted.contains("\"level\":\"INFO\""))
        assertTrue(formatted.contains("\"logger\":\"TestLogger\""))
        assertTrue(formatted.contains("\"message\":\"Test message\""))
    }

    @Test
    fun testJsonFormatterWithAttributes() {
        val formatter = JsonLogFormatter()
        val event = createTestEvent(attributes = mapOf("key" to "value"))
        val formatted = formatter.format(event)

        assertTrue(formatted.contains("\"attributes\":"))
        assertTrue(formatted.contains("\"key\":\"value\""))
    }

    @Test
    fun testLogFormattersFactory() {
        val default = LogFormatters.default()
        val pretty = LogFormatters.pretty()
        val compact = LogFormatters.compact()
        val json = LogFormatters.json()

        assertTrue(default is DefaultLogFormatter)
        assertTrue(pretty is PrettyLogFormatter)
        assertTrue(compact is CompactLogFormatter)
        assertTrue(json is JsonLogFormatter)
    }
}
