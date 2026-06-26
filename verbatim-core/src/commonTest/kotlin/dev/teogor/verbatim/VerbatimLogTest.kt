package dev.teogor.verbatim

import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LoggerConfig
import dev.teogor.verbatim.core.sinks.TestLogSink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VerbatimLogTest {

    @Test
    fun testSetDefaultTag() {
        VerbatimLog.setDefaultTag("MyApp")
        val logger = VerbatimLog.withTag("Test")
        assertNotNull(logger)
    }

    @Test
    fun testWithTag() {
        val logger = VerbatimLog.withTag("MyComponent")
        assertNotNull(logger)
        assertEquals("MyComponent", logger.tag)
    }

    @Test
    fun testWithClassTag() {
        val logger = VerbatimLog.withClassTag(String::class)
        assertNotNull(logger)
        assertEquals("String", logger.tag)
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
        
        VerbatimLog.v("TestTag") { "Verbose message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.VERBOSE, testSink.events[0].level)
        assertEquals("Verbose message", testSink.events[0].message)
        
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
        
        VerbatimLog.d("TestTag") { "Debug message" }
        
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
        
        VerbatimLog.i("TestTag") { "Info message" }
        
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
        
        VerbatimLog.w("TestTag") { "Warning message" }
        
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
        
        VerbatimLog.e("TestTag") { "Error message" }
        
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
        
        VerbatimLog.fatal("TestTag") { "Fatal message" }
        
        assertEquals(1, testSink.events.size)
        assertEquals(LogLevel.FATAL, testSink.events[0].level)
        assertEquals("Fatal message", testSink.events[0].message)
        
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
        
        VerbatimLog.i(
            tag = "TestTag",
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
