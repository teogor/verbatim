package dev.teogor.verbatim

import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LoggerConfig
import dev.teogor.verbatim.core.sinks.TestLogSink
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VerbatimTest {
    @Test
    fun testInstallation() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(testSink)
                .build()
        )
        val logger = Verbatim.logger("TestTag")
        assertNotNull(logger)
        assertEquals("TestTag", logger.tag)
        Verbatim.uninstall()
    }

    @Test
    fun testLogging() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(testSink)
                .build()
        )
        
        val logger = Verbatim.logger("TestTag")
        logger.debug { "Test debug message" }
        logger.info { "Test info message" }
        
        assertEquals(2, testSink.events.size)
        assertEquals("Test debug message", testSink.events[0].message)
        assertEquals("Test info message", testSink.events[1].message)
        
        Verbatim.uninstall()
    }

    @Test
    fun testLevelFiltering() {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.INFO)
                .addSink(testSink)
                .build()
        )
        
        val logger = Verbatim.logger("TestTag")
        logger.debug { "This should be filtered" }
        logger.info { "This should be logged" }
        
        assertEquals(1, testSink.events.size)
        assertEquals("This should be logged", testSink.events[0].message)
        
        Verbatim.uninstall()
    }
}
