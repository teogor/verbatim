package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.sinks.TestLogSink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LoggerTest {

    @Test
    fun testLoggerCreation() {
        val logger = Logger("TestTag")
        assertEquals("TestTag", logger.tag)
    }

    @Test
    fun testVerboseLogging() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.VERBOSE
        
        val logger = Logger("TestTag")
        logger.verbose { "Verbose message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.VERBOSE, testSink.events[0].level)
        assertEquals("Verbose message", testSink.events[0].message)
        assertEquals("TestTag", testSink.events[0].loggerName)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testDebugLogging() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.DEBUG
        
        val logger = Logger("TestTag")
        logger.debug { "Debug message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.DEBUG, testSink.events[0].level)
        assertEquals("Debug message", testSink.events[0].message)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testInfoLogging() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.INFO
        
        val logger = Logger("TestTag")
        logger.info { "Info message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.INFO, testSink.events[0].level)
        assertEquals("Info message", testSink.events[0].message)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testWarnLogging() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.WARN
        
        val logger = Logger("TestTag")
        logger.warn { "Warning message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.WARN, testSink.events[0].level)
        assertEquals("Warning message", testSink.events[0].message)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testErrorLogging() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.ERROR
        
        val logger = Logger("TestTag")
        logger.error { "Error message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.ERROR, testSink.events[0].level)
        assertEquals("Error message", testSink.events[0].message)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testFatalLogging() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.FATAL
        
        val logger = Logger("TestTag")
        logger.fatal { "Fatal message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.FATAL, testSink.events[0].level)
        assertEquals("Fatal message", testSink.events[0].message)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testLoggingWithThrowable() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.ERROR
        
        val logger = Logger("TestTag")
        val exception = RuntimeException("Test exception")
        logger.error(throwable = exception) { "Error with exception" }
        
        assertEquals(1, testSink.events.size)
        assertNotNull(testSink.events[0].throwable)
        assertEquals("Test exception", testSink.events[0].throwable?.message)
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testLoggingWithAttributes() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.INFO
        
        val logger = Logger("TestTag")
        logger.info(
            attributes = {
                attr("user_id", "12345")
                attr("action", "login")
            }
        ) { "User logged in" }
        
        assertEquals(1, testSink.events.size)
        assertEquals("12345", testSink.events[0].attributes["user_id"])
        assertEquals("login", testSink.events[0].attributes["action"])
        
        VerbatimPipeline.removeSink(testSink)
    }

    @Test
    fun testLevelFiltering() {
        val testSink = TestLogSink()
        VerbatimPipeline.addSink(testSink)
        LogPipeline.minimumLogLevel = LogLevel.INFO
        
        val logger = Logger("TestTag")
        logger.debug { "This should be filtered" }
        logger.info { "This should be logged" }
        
        assertEquals(1, testSink.events.size)
        assertEquals("This should be logged", testSink.events[0].message)
        
        VerbatimPipeline.removeSink(testSink)
    }
}
