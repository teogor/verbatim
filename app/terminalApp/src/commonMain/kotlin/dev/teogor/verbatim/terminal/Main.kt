package dev.teogor.verbatim.terminal

import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LogFormatters
import dev.teogor.verbatim.core.LoggerConfig
import dev.teogor.verbatim.core.sinks.RemoteLogSink
import dev.teogor.verbatim.core.sinks.TestLogSink
import dev.teogor.verbatim.core.visuals.LogVisuals
import dev.teogor.verbatim.shared.core.getPlatform
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val platform = getPlatform()
    println("========================================")
    println("  Verbatim Logging Library Demo")
    println("  Platform: ${platform.name}")
    println("========================================")
    println()

    // --- Section 1: Basic Logging ---
    section("1. Basic Logging") {
        Verbatim.install(
            minLevel = LogLevel.VERBOSE,
            visuals = LogVisuals.Emojis
        )
        val log = Verbatim.logger("Basic")

        log.verbose { "A verbose message (developer detail)" }
        log.debug { "A debug message" }
        log.info { "An informational message" }
        log.warn { "A warning message" }
        log.error { "An error message" }
    }

    // --- Section 2: Structured Attributes ---
    section("2. Structured Attributes") {
        val log = Verbatim.logger("Auth")

        log.info(
            attributes = {
                attr("user_id", 42)
                attr("action", "login")
                attr("method", "oauth2")
            }
        ) { "User authenticated successfully" }

        log.warn(
            attributes = {
                attr("user_id", 99)
                attr("attempts", 3)
                attr("ip", "192.168.1.100")
            }
        ) { "Multiple failed login attempts" }

        log.error(
            throwable = RuntimeException("Connection refused"),
            attributes = {
                attr("host", "db.example.com")
                attr("port", 5432)
                attr("timeout_ms", 5000)
            }
        ) { "Database connection failed" }
    }

    // --- Section 3: Visual Themes ---
    section("3. Visual Themes - Emojis") {
        Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Emojis)
        val log = Verbatim.logger("Themes.Emoji")
        log.debug { "Debug with Emojis theme" }
        log.info { "Info with Emojis theme" }
        log.warn { "Warn with Emojis theme" }
        log.error { "Error with Emojis theme" }
    }

    section("3. Visual Themes - Geometric") {
        Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Geometric)
        val log = Verbatim.logger("Themes.Geometric")
        log.debug { "Debug with Geometric theme" }
        log.info { "Info with Geometric theme" }
        log.warn { "Warn with Geometric theme" }
        log.error { "Error with Geometric theme" }
    }

    section("3. Visual Themes - Minimalist") {
        Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Minimalist)
        val log = Verbatim.logger("Themes.Minimalist")
        log.debug { "Debug with Minimalist theme" }
        log.info { "Info with Minimalist theme" }
        log.warn { "Warn with Minimalist theme" }
        log.error { "Error with Minimalist theme" }
    }

    section("3. Visual Themes - NerdFonts") {
        Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.NerdFonts)
        val log = Verbatim.logger("Themes.NerdFonts")
        log.debug { "Debug with NerdFonts theme" }
        log.info { "Info with NerdFonts theme" }
        log.warn { "Warn with NerdFonts theme" }
        log.error { "Error with NerdFonts theme" }
    }

    section("3. Visual Themes - AnsiColors") {
        Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.AnsiColors)
        val log = Verbatim.logger("Themes.AnsiColors")
        log.debug { "Debug with AnsiColors theme" }
        log.info { "Info with AnsiColors theme" }
        log.warn { "Warn with AnsiColors theme" }
        log.error { "Error with AnsiColors theme" }
    }

    // --- Section 4: Formatters ---
    section("4. Formatters - Pretty") {
        Verbatim.install(
            minLevel = LogLevel.DEBUG,
            visuals = LogVisuals.Geometric
        )
        val log = Verbatim.logger("Formatter.Pretty")
        log.info(
            attributes = {
                attr("request_id", "req-001")
                attr("latency_ms", 234)
            }
        ) { "GET /api/users - 200 OK" }
    }

    section("4. Formatters - JSON") {
        Verbatim.install(minLevel = LogLevel.DEBUG)
        val log = Verbatim.logger("Formatter.Json")
        log.info(
            attributes = {
                attr("request_id", "req-002")
                attr("method", "POST")
                attr("status", 201)
            }
        ) { "Created user" }
    }

    // --- Section 5: Per-Tag Level Overrides ---
    section("5. Per-Tag Level Overrides") {
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.WARN)
                .override("NetworkModule", LogLevel.VERBOSE)
                .override("DbModule", LogLevel.DEBUG)
                .build()
        )

        val netLog = Verbatim.logger("NetworkModule")
        val dbLog = Verbatim.logger("DbModule")
        val appLog = Verbatim.logger("AppModule")

        netLog.debug { "This WILL appear (overridden to VERBOSE)" }
        dbLog.debug { "This WILL appear (overridden to DEBUG)" }
        appLog.debug { "This will NOT appear (global minimum is WARN)" }
        appLog.warn { "This WILL appear (meets global minimum)" }
    }

    // --- Section 6: Custom Sink (RemoteLogSink) ---
    section("6. Custom Sink - RemoteLogSink") {
        val capturedLogs = mutableListOf<String>()

        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(RemoteLogSink { formatted -> capturedLogs.add(formatted) })
                .build()
        )

        val log = Verbatim.logger("Remote")
        log.info { "This goes to remote sink" }
        log.warn { "Warning to remote" }

        println("  Captured ${capturedLogs.size} logs in remote sink:")
        capturedLogs.forEach { println("    -> $it") }
    }

    // --- Section 7: TestLogSink ---
    section("7. TestLogSink - In-Memory Capture") {
        val testSink = TestLogSink()

        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(testSink)
                .build()
        )

        val log = Verbatim.logger("Test")
        log.info { "Info for test" }
        log.warn { "Warning for test" }
        log.error { "Error for test" }

        println("  Total events captured: ${testSink.events.size}")
        println("  Has WARN+ events: ${testSink.hasLevel(LogLevel.WARN)}")
        println("  Last event level: ${testSink.lastEvent()?.level}")
        println("  Last event message: ${testSink.lastEvent()?.message}")
    }

    // --- Section 8: Global Attributes ---
    section("8. Global Attributes") {
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .globalAttribute("app_version", "1.0.0-alpha01")
                .globalAttribute("build_type", "debug")
                .globalAttribute("platform", platform.name)
                .build()
        )

        val log = Verbatim.logger("Global")
        log.info { "App started with global context" }
    }

    // --- Section 9: Exception Logging ---
    section("9. Exception Logging") {
        Verbatim.install(minLevel = LogLevel.DEBUG)
        val log = Verbatim.logger("Exceptions")

        try {
            throw IllegalStateException("Something went wrong")
        } catch (e: Exception) {
            log.error(throwable = e) { "Caught exception" }
        }

        log.error(
            throwable = IllegalArgumentException("Invalid parameter"),
            attributes = {
                attr("param", "timeout")
                attr("value", -1)
            }
        ) { "Validation failed" }
    }

    // --- Section 10: FATAL Level ---
    section("10. FATAL Level (throws RuntimeException)") {
        Verbatim.install(minLevel = LogLevel.DEBUG)
        val log = Verbatim.logger("Fatal")

        try {
            log.fatal { "Critical system failure" }
        } catch (e: RuntimeException) {
            println("  Caught FATAL exception: ${e.message}")
        }
    }

    println()
    println("========================================")
    println("  Demo Complete")
    println("========================================")
}

private fun section(title: String, block: () -> Unit) {
    println("--- $title ---")
    block()
    println()
}
