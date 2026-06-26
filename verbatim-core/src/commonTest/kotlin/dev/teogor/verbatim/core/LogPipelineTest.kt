package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.sinks.TestLogSink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LogPipelineTest {

    @Test
    fun testProcessEventAboveMinimumLevel() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        
        LogPipeline.minimumLogLevel = LogLevel.INFO
        
        val event = LogEvent(
            level = LogLevel.INFO,
            loggerName = "TestLogger",
            message = "Test message",
            throwable = null,
            attributes = emptyMap(),
            context = LogContext(),
            thread = "main",
            timestamp = kotlin.time.Instant.fromEpochMilliseconds(0)
        )
        
        val result = LogPipeline.process(event)
        
        assertTrue(result)
        assertEquals(1, testSink.events.size)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testProcessEventBelowMinimumLevel() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        
        LogPipeline.minimumLogLevel = LogLevel.INFO
        
        val event = LogEvent(
            level = LogLevel.DEBUG,
            loggerName = "TestLogger",
            message = "Test message",
            throwable = null,
            attributes = emptyMap(),
            context = LogContext(),
            thread = "main",
            timestamp = kotlin.time.Instant.fromEpochMilliseconds(0)
        )
        
        val result = LogPipeline.process(event)
        
        assertFalse(result)
        assertEquals(0, testSink.events.size)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testTagOverride() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        
        LogPipeline.minimumLogLevel = LogLevel.INFO
        LogPipeline.override("NetworkModule", LogLevel.VERBOSE)
        
        val debugEvent = LogEvent(
            level = LogLevel.DEBUG,
            loggerName = "NetworkModule",
            message = "Debug message",
            throwable = null,
            attributes = emptyMap(),
            context = LogContext(),
            thread = "main",
            timestamp = kotlin.time.Instant.fromEpochMilliseconds(0)
        )
        
        val result = LogPipeline.process(debugEvent)
        
        assertTrue(result)
        assertEquals(1, testSink.events.size)
        
        VerbatimPipeline.removeSink(testSink)
        LogPipeline.clearOverrides()
    }

    @Test
    fun testRemoveTagOverride() {
        LogPipeline.minimumLogLevel = LogLevel.INFO
        LogPipeline.override("TestTag", LogLevel.DEBUG)
        
        assertEquals(LogLevel.DEBUG, LogPipeline.getLevelForTag("TestTag"))
        
        LogPipeline.removeOverride("TestTag")
        
        assertEquals(LogLevel.INFO, LogPipeline.getLevelForTag("TestTag"))
    }

    @Test
    fun testClearOverrides() {
        LogPipeline.minimumLogLevel = LogLevel.INFO
        LogPipeline.override("Tag1", LogLevel.DEBUG)
        LogPipeline.override("Tag2", LogLevel.VERBOSE)
        
        LogPipeline.clearOverrides()
        
        assertEquals(LogLevel.INFO, LogPipeline.getLevelForTag("Tag1"))
        assertEquals(LogLevel.INFO, LogPipeline.getLevelForTag("Tag2"))
    }

    @Test
    fun testGetLevelForTagWithoutOverride() {
        LogPipeline.minimumLogLevel = LogLevel.WARN
        
        assertEquals(LogLevel.WARN, LogPipeline.getLevelForTag("AnyTag"))
    }
}
