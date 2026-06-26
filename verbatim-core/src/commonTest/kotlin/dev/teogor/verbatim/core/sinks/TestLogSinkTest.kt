package dev.teogor.verbatim.core.sinks

import dev.teogor.verbatim.core.LogContext
import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestLogSinkTest {

    private fun createTestEvent(
        level: LogLevel = LogLevel.INFO,
        message: String = "Test message"
    ): LogEvent {
        return LogEvent(
            level = level,
            loggerName = "TestLogger",
            message = message,
            throwable = null,
            attributes = emptyMap(),
            context = LogContext(),
            thread = "main",
            timestamp = kotlin.time.Instant.fromEpochMilliseconds(0)
        )
    }

    @Test
    fun testEmitAndCapture() {
        val sink = TestLogSink()
        val event = createTestEvent()
        
        sink.emit(event)
        
        assertEquals(1, sink.events.size)
        assertEquals(event, sink.events[0])
    }

    @Test
    fun testClear() {
        val sink = TestLogSink()
        sink.emit(createTestEvent())
        sink.emit(createTestEvent())
        
        assertEquals(2, sink.size)
        
        sink.clear()
        
        assertEquals(0, sink.size)
    }

    @Test
    fun testHasLevel() {
        val sink = TestLogSink()
        sink.emit(createTestEvent(level = LogLevel.INFO))
        sink.emit(createTestEvent(level = LogLevel.ERROR))
        
        assertTrue(sink.hasLevel(LogLevel.INFO))
        assertTrue(sink.hasLevel(LogLevel.ERROR))
        assertFalse(sink.hasLevel(LogLevel.DEBUG))
    }

    @Test
    fun testMessagesWithLevel() {
        val sink = TestLogSink()
        sink.emit(createTestEvent(level = LogLevel.INFO, message = "Info 1"))
        sink.emit(createTestEvent(level = LogLevel.ERROR, message = "Error 1"))
        sink.emit(createTestEvent(level = LogLevel.INFO, message = "Info 2"))
        
        val infoMessages = sink.messagesWithLevel(LogLevel.INFO)
        assertEquals(2, infoMessages.size)
        assertTrue(infoMessages.contains("Info 1"))
        assertTrue(infoMessages.contains("Info 2"))
        
        val errorMessages = sink.messagesWithLevel(LogLevel.ERROR)
        assertEquals(1, errorMessages.size)
        assertTrue(errorMessages.contains("Error 1"))
    }

    @Test
    fun testLastEvent() {
        val sink = TestLogSink()
        assertNull(sink.lastEvent())
        
        val event1 = createTestEvent(message = "First")
        val event2 = createTestEvent(message = "Second")
        sink.emit(event1)
        sink.emit(event2)
        
        assertNotNull(sink.lastEvent())
        assertEquals("Second", sink.lastEvent()?.message)
    }

    @Test
    fun testLastMessage() {
        val sink = TestLogSink()
        assertNull(sink.lastMessage())
        
        sink.emit(createTestEvent(message = "First"))
        sink.emit(createTestEvent(message = "Second"))
        
        assertEquals("Second", sink.lastMessage())
    }

    @Test
    fun testSize() {
        val sink = TestLogSink()
        assertEquals(0, sink.size)
        
        sink.emit(createTestEvent())
        assertEquals(1, sink.size)
        
        sink.emit(createTestEvent())
        assertEquals(2, sink.size)
    }

    @Test
    fun testEventsIsImmutable() {
        val sink = TestLogSink()
        sink.emit(createTestEvent())
        
        val events = sink.events
        assertEquals(1, events.size)
        
        sink.emit(createTestEvent())
        assertEquals(1, events.size) // Original list should not change
        assertEquals(2, sink.events.size) // New snapshot should have 2
    }
}
