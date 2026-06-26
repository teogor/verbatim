package dev.teogor.verbatim.crashlytics

import dev.teogor.verbatim.core.LogContext
import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

class CrashSinkTest {

    private class MockCrashEngine : CrashReportEngine {
        val recordedExceptions = mutableListOf<Pair<Throwable, Map<String, Any?>>>()
        val loggedMessages = mutableListOf<Pair<String, CrashSeverity>>()

        override fun recordException(t: Throwable, attributes: Map<String, Any?>) {
            recordedExceptions.add(t to attributes)
        }

        override fun log(message: String, severity: CrashSeverity) {
            loggedMessages.add(message to severity)
        }
    }

    @Test
    fun crashSinkForwardsExceptions() {
        val engine = MockCrashEngine()
        val sink = CrashSink().apply {
            this.engine = engine
            minLevel = LogLevel.ERROR
        }

        val exception = RuntimeException("Test error")
        val event = LogEvent(
            level = LogLevel.ERROR,
            loggerName = "Test",
            message = "Something failed",
            throwable = exception,
            attributes = mapOf("key" to "value"),
            context = LogContext(),
            thread = "main",
            timestamp = Clock.System.now()
        )

        sink.emit(event)

        assertEquals(1, engine.recordedExceptions.size)
        assertEquals(exception, engine.recordedExceptions[0].first)
        assertTrue(engine.recordedExceptions[0].second.containsKey("key"))
    }

    @Test
    fun crashSinkForwardsLogsAboveMinLevel() {
        val engine = MockCrashEngine()
        val sink = CrashSink().apply {
            this.engine = engine
            minLevel = LogLevel.WARN
        }

        val event = LogEvent(
            level = LogLevel.WARN,
            loggerName = "Test",
            message = "Warning message",
            throwable = null,
            attributes = emptyMap(),
            context = LogContext(),
            thread = "main",
            timestamp = Clock.System.now()
        )

        sink.emit(event)

        assertEquals(1, engine.loggedMessages.size)
        assertEquals(CrashSeverity.WARNING, engine.loggedMessages[0].second)
    }

    @Test
    fun crashSinkDropsLogsBelowMinLevel() {
        val engine = MockCrashEngine()
        val sink = CrashSink().apply {
            this.engine = engine
            minLevel = LogLevel.ERROR
        }

        val event = LogEvent(
            level = LogLevel.INFO,
            loggerName = "Test",
            message = "Info message",
            throwable = null,
            attributes = emptyMap(),
            context = LogContext(),
            thread = "main",
            timestamp = Clock.System.now()
        )

        sink.emit(event)

        assertEquals(0, engine.loggedMessages.size)
        assertEquals(0, engine.recordedExceptions.size)
    }
}
