package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.visuals.DefaultLogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LogLevelVisualTest {

    @Test
    fun testLogLevelWeights() {
        assertEquals(1, LogLevel.VERBOSE.weight)
        assertEquals(2, LogLevel.DEBUG.weight)
        assertEquals(3, LogLevel.INFO.weight)
        assertEquals(4, LogLevel.WARN.weight)
        assertEquals(5, LogLevel.ERROR.weight)
        assertEquals(6, LogLevel.FATAL.weight)
        assertEquals(Int.MAX_VALUE, LogLevel.OFF.weight)
    }

    @Test
    fun testIsAtLeast() {
        assertTrue(LogLevel.FATAL.isAtLeast(LogLevel.ERROR))
        assertTrue(LogLevel.ERROR.isAtLeast(LogLevel.WARN))
        assertTrue(LogLevel.WARN.isAtLeast(LogLevel.INFO))
        assertTrue(LogLevel.INFO.isAtLeast(LogLevel.DEBUG))
        assertTrue(LogLevel.DEBUG.isAtLeast(LogLevel.VERBOSE))

        assertTrue(LogLevel.FATAL.isAtLeast(LogLevel.VERBOSE))
        assertTrue(!LogLevel.VERBOSE.isAtLeast(LogLevel.ERROR))
    }

    @Test
    fun testFromWeight() {
        assertEquals(LogLevel.VERBOSE, LogLevel.fromWeight(1))
        assertEquals(LogLevel.DEBUG, LogLevel.fromWeight(2))
        assertEquals(LogLevel.INFO, LogLevel.fromWeight(3))
        assertEquals(LogLevel.WARN, LogLevel.fromWeight(4))
        assertEquals(LogLevel.ERROR, LogLevel.fromWeight(5))
        assertEquals(LogLevel.FATAL, LogLevel.fromWeight(6))
        assertEquals(LogLevel.OFF, LogLevel.fromWeight(Int.MAX_VALUE))
        assertEquals(null, LogLevel.fromWeight(999))
    }

    @Test
    fun testEmojisTheme() {
        val visuals = LogVisuals.Emojis
        assertEquals("💜", visuals.indicator.getIndicator(LogLevel.VERBOSE))
        assertEquals("💚", visuals.indicator.getIndicator(LogLevel.DEBUG))
        assertEquals("💙", visuals.indicator.getIndicator(LogLevel.INFO))
        assertEquals("💛", visuals.indicator.getIndicator(LogLevel.WARN))
        assertEquals("❤️", visuals.indicator.getIndicator(LogLevel.ERROR))
        assertEquals("💔", visuals.indicator.getIndicator(LogLevel.FATAL))
    }

    @Test
    fun testGeometricTheme() {
        val visuals = LogVisuals.Geometric
        assertEquals("🟪", visuals.indicator.getIndicator(LogLevel.VERBOSE))
        assertEquals("🟩", visuals.indicator.getIndicator(LogLevel.DEBUG))
        assertEquals("🟦", visuals.indicator.getIndicator(LogLevel.INFO))
        assertEquals("🟨", visuals.indicator.getIndicator(LogLevel.WARN))
        assertEquals("🟥", visuals.indicator.getIndicator(LogLevel.ERROR))
        assertEquals("⬛", visuals.indicator.getIndicator(LogLevel.FATAL))
    }

    @Test
    fun testMinimalistTheme() {
        val visuals = LogVisuals.Minimalist
        assertEquals("", visuals.indicator.getIndicator(LogLevel.VERBOSE))
        assertEquals("", visuals.indicator.getIndicator(LogLevel.DEBUG))
        assertEquals("", visuals.indicator.getIndicator(LogLevel.INFO))
        assertEquals("", visuals.indicator.getIndicator(LogLevel.WARN))
        assertEquals("", visuals.indicator.getIndicator(LogLevel.ERROR))
        assertEquals("", visuals.indicator.getIndicator(LogLevel.FATAL))
    }

    @Test
    fun testNerdFontsTheme() {
        val visuals = LogVisuals.NerdFonts
        assertNotNull(visuals.indicator.getIndicator(LogLevel.VERBOSE))
        assertNotNull(visuals.indicator.getIndicator(LogLevel.DEBUG))
        assertNotNull(visuals.indicator.getIndicator(LogLevel.INFO))
        assertNotNull(visuals.indicator.getIndicator(LogLevel.WARN))
        assertNotNull(visuals.indicator.getIndicator(LogLevel.ERROR))
        assertNotNull(visuals.indicator.getIndicator(LogLevel.FATAL))
    }

    @Test
    fun testAnsiColorsTheme() {
        val visuals = LogVisuals.AnsiColors
        assertTrue(visuals.indicator.getIndicator(LogLevel.VERBOSE).contains("\u001B[35m"))
        assertTrue(visuals.indicator.getIndicator(LogLevel.DEBUG).contains("\u001B[32m"))
        assertTrue(visuals.indicator.getIndicator(LogLevel.INFO).contains("\u001B[34m"))
        assertTrue(visuals.indicator.getIndicator(LogLevel.WARN).contains("\u001B[33m"))
        assertTrue(visuals.indicator.getIndicator(LogLevel.ERROR).contains("\u001B[31m"))
        assertTrue(visuals.indicator.getIndicator(LogLevel.FATAL).contains("\u001B[1;31m"))
    }

    @Test
    fun testCustomVisuals() {
        val custom = LogVisuals.custom {
            on(LogLevel.INFO) { indicator = "ℹ️"; label = "INF" }
            on(LogLevel.ERROR) { indicator = "❌"; label = "ERR" }
        }

        assertEquals("ℹ️", custom.indicator.getIndicator(LogLevel.INFO))
        assertEquals("❌", custom.indicator.getIndicator(LogLevel.ERROR))
        assertEquals("INF MyApp", custom.labelFormatter(LogLevel.INFO, "MyApp"))
        assertEquals("ERR MyApp", custom.labelFormatter(LogLevel.ERROR, "MyApp"))
    }

    @Test
    fun testRawVisuals() {
        val raw = LogVisuals.raw { level, tag ->
            "[${level.name}] $tag"
        }

        assertEquals("[INFO] MyApp", raw.labelFormatter(LogLevel.INFO, "MyApp"))
        assertEquals("[ERROR] Network", raw.labelFormatter(LogLevel.ERROR, "Network"))
    }

    @Test
    fun testAdaptiveVisuals() {
        val adaptive = LogVisuals.adaptive()
        assertNotNull(adaptive)
        assertNotNull(adaptive.indicator)
        assertNotNull(adaptive.labelFormatter)
    }
}
