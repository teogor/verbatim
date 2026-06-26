package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.sinks.TestLogSink
import dev.teogor.verbatim.Verbatim
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TaggedLoggerTest {

    @Test
    fun testTaggedLoggerCreation() {
        val logger = TaggedLogger("MyComponent")
        assertNotNull(logger)
    }

    @Test
    fun testVerboseLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.VERBOSE)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.v { "Verbose message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.VERBOSE, testSink.events[0].level)
        assertEquals("Verbose message", testSink.events[0].message)
        assertEquals("TestTag", testSink.events[0].loggerName)
        
        Verbatim.uninstall()
    }

    @Test
    fun testDebugLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.d { "Debug message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.DEBUG, testSink.events[0].level)
        assertEquals("Debug message", testSink.events[0].message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testInfoLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.INFO)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.i { "Info message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.INFO, testSink.events[0].level)
        assertEquals("Info message", testSink.events[0].message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testWarnLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.WARN)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.w { "Warning message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.WARN, testSink.events[0].level)
        assertEquals("Warning message", testSink.events[0].message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testErrorLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.ERROR)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.e { "Error message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.ERROR, testSink.events[0].level)
        assertEquals("Error message", testSink.events[0].message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testFatalLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.FATAL)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.fatal { "Fatal message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.FATAL, testSink.events[0].level)
        assertEquals("Fatal message", testSink.events[0].message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testLoggingWithThrowable() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.ERROR)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        val exception = RuntimeException("Test exception")
        logger.e(throwable = exception) { "Error with exception" }
        
        assertEquals(1, testSink.events.size)
        assertNotNull(testSink.events[0].throwable)
        assertEquals("Test exception", testSink.events[0].throwable?.message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testLoggingWithAttributes() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.INFO)
                .addSink(testSink)
                .build()
        )
        
        val logger = TaggedLogger("TestTag")
        logger.i(
            attributes = {
                attr("user_id", "12345")
                attr("action", "login")
            }
        ) { "User logged in" }
        
        assertEquals(1, testSink.events.size)
        assertEquals("12345", testSink.events[0].attributes["user_id"])
        assertEquals("login", testSink.events[0].attributes["action"])
        
        Verbatim.uninstall()
    }
}
