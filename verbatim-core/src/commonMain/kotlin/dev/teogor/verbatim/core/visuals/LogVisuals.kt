package dev.teogor.verbatim.core.visuals

import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.Platform

/**
 * Built-in visual themes and factory methods for log formatting.
 *
 * This object provides:
 * - Pre-built themes (Emojis, Geometric, Minimalist, NerdFonts, AnsiColors)
 * - Custom theme builder
 * - Raw provider for complete control
 * - Adaptive theme selection based on platform
 *
 * Example usage:
 * ```kotlin
 * // Use a built-in theme
 * val visuals = LogVisuals.Emojis
 *
 * // Create a custom theme
 * val custom = LogVisuals.custom {
 *     on(LogLevel.VERBOSE) { indicator = "🔍"; label = "VRB" }
 *     on(LogLevel.DEBUG)   { indicator = "🛠️"; label = "DBG" }
 * }
 * ```
 */
object LogVisuals {
    /**
     * Playful emoji theme using hearts.
     *
     * Output: `💜 VERBOSE`, `💚 DEBUG`, `💙 INFO`, `💛 WARN`, `❤️ ERROR`, `💔 FATAL`
     */
    val Emojis: LogVisualConfig = customVisuals {
        on(LogLevel.VERBOSE) { indicator = "💜"; label = "VERBOSE" }
        on(LogLevel.DEBUG) { indicator = "💚"; label = "DEBUG" }
        on(LogLevel.INFO) { indicator = "💙"; label = "INFO" }
        on(LogLevel.WARN) { indicator = "💛"; label = "WARN" }
        on(LogLevel.ERROR) { indicator = "❤️"; label = "ERROR" }
        on(LogLevel.FATAL) { indicator = "💔"; label = "FATAL" }
    }

    /**
     * Geometric minimalist theme using colored squares.
     *
     * Output: `🟪 VERB`, `🟩 DEBG`, `🟦 INFO`, `🟨 WARN`, `🟥 ERRR`, `⬛ FTL `
     */
    val Geometric: LogVisualConfig = customVisuals {
        on(LogLevel.VERBOSE) { indicator = "🟪"; label = "VERB" }
        on(LogLevel.DEBUG) { indicator = "🟩"; label = "DEBG" }
        on(LogLevel.INFO) { indicator = "🟦"; label = "INFO" }
        on(LogLevel.WARN) { indicator = "🟨"; label = "WARN" }
        on(LogLevel.ERROR) { indicator = "🟥"; label = "ERRR" }
        on(LogLevel.FATAL) { indicator = "⬛"; label = "FTL " }
    }

    /**
     * Minimalist bracket theme for clean, low-fidelity output.
     *
     * Output: `[V] tag`, `[D] tag`, `[I] tag`, `[W] tag`, `[E] tag`, `[F] tag`
     */
    val Minimalist: LogVisualConfig = customVisuals {
        on(LogLevel.VERBOSE) { indicator = ""; label = "[V]" }
        on(LogLevel.DEBUG) { indicator = ""; label = "[D]" }
        on(LogLevel.INFO) { indicator = ""; label = "[I]" }
        on(LogLevel.WARN) { indicator = ""; label = "[W]" }
        on(LogLevel.ERROR) { indicator = ""; label = "[E]" }
        on(LogLevel.FATAL) { indicator = ""; label = "[F]" }
    }

    /**
     * Nerd Font theme using powerline glyphs.
     *
     * Output: ` VRB tag`, ` DBG tag`, ` INF tag`, ` WRN tag`, ` ERR tag`, ` FTL tag`
     */
    val NerdFonts: LogVisualConfig = customVisuals {
        on(LogLevel.VERBOSE) { indicator = "\uE791"; label = "VRB" }  // nf-oct-markdown
        on(LogLevel.DEBUG) { indicator = "\uEB88"; label = "DBG" }  // nf-oct-tools
        on(LogLevel.INFO) { indicator = "\uE795"; label = "INF" }  // nf-oct-info
        on(LogLevel.WARN) { indicator = "\uEA07"; label = "WRN" }  // nf-oct-alert
        on(LogLevel.ERROR) { indicator = "\uE797"; label = "ERR" }  // nf-oct-x
        on(LogLevel.FATAL) { indicator = "\uE740"; label = "FTL" }  // nf-oct-squirrel (skull)
    }

    /**
     * ANSI color theme for terminal output.
     *
     * Uses ANSI escape codes to colorize log levels:
     * - VERBOSE: Purple
     * - DEBUG: Green
     * - INFO: Blue
     * - WARN: Yellow
     * - ERROR: Red
     * - FATAL: Bold Red
     */
    val AnsiColors: LogVisualConfig = customVisuals {
        on(LogLevel.VERBOSE) { indicator = "\u001B[35m"; label = "VERBOSE" }  // Purple
        on(LogLevel.DEBUG) { indicator = "\u001B[32m"; label = "DEBUG" }    // Green
        on(LogLevel.INFO) { indicator = "\u001B[34m"; label = "INFO" }     // Blue
        on(LogLevel.WARN) { indicator = "\u001B[33m"; label = "WARN" }     // Yellow
        on(LogLevel.ERROR) { indicator = "\u001B[31m"; label = "ERROR" }    // Red
        on(LogLevel.FATAL) { indicator = "\u001B[1;31m"; label = "FATAL" }  // Bold Red
    }

    /**
     * Create a custom visual configuration using a DSL.
     *
     * Example:
     * ```kotlin
     * val custom = LogVisuals.custom {
     *     on(LogLevel.VERBOSE) { indicator = "🔍"; label = "VRB" }
     *     on(LogLevel.DEBUG)   { indicator = "🛠️"; label = "DBG" }
     * }
     * ```
     *
     * @param block Configuration block.
     * @return A new [LogVisualConfig] instance.
     */
    fun custom(block: LogVisualsBuilder.() -> Unit): LogVisualConfig {
        return LogVisualsBuilder().apply(block).build()
    }

    /**
     * Create a visual configuration with a raw provider function.
     *
     * This gives complete control over the visual output format.
     *
     * Example:
     * ```kotlin
     * val raw = LogVisuals.raw { level, tag ->
     *     val timestamp = Clock.System.now().formatTime()
     *     "$timestamp [$tag] ${level.name}"
     * }
     * ```
     *
     * @param provider Function that receives level and tag, returns formatted prefix.
     * @return A new [LogVisualConfig] instance.
     */
    fun raw(provider: (LogLevel, String) -> String): LogVisualConfig {
        return DefaultLogVisualConfig(
            indicator = LogLevelVisual { _ -> "" },
            labelFormatter = { level, tag -> provider(level, tag) }
        )
    }

    /**
     * Create an adaptive visual configuration based on platform.
     *
     * Example:
     * ```kotlin
     * val adaptive = LogVisuals.adaptive()
     * val customAdaptive = LogVisuals.adaptive { platform ->
     *     when {
     *         platform.isTerminalAnsiCompliant -> LogVisuals.AnsiColors
     *         else -> LogVisuals.Minimalist
     *     }
     * }
     * ```
     *
     * @param platformProvider Function to get current platform info.
     * @param themeSelector Function to select theme based on platform.
     * @return A new [LogVisualConfig] instance.
     */
    fun adaptive(
        platformProvider: () -> PlatformInfo = { PlatformInfo.current() },
        themeSelector: (PlatformInfo) -> LogVisualConfig = { defaults(it) }
    ): LogVisualConfig {
        return AdaptiveLogVisualConfig(platformProvider, themeSelector)
    }

    private fun defaults(platform: PlatformInfo): LogVisualConfig {
        return when {
            platform.isTerminalAnsiCompliant -> AnsiColors
            platform.isMobileIdeConsole -> Emojis
            platform.isCiCdEnvironment -> Minimalist
            else -> Geometric
        }
    }
}

/**
 * Platform information for adaptive theme selection.
 *
 * @property isTerminalAnsiCompliant Whether the terminal supports ANSI escape codes.
 * @property isMobileIdeConsole Whether running in a mobile IDE console.
 * @property isCiCdEnvironment Whether running in a CI/CD environment.
 * @property os The operating system name.
 */
data class PlatformInfo(
    val isTerminalAnsiCompliant: Boolean = false,
    val isMobileIdeConsole: Boolean = false,
    val isCiCdEnvironment: Boolean = false,
    val os: String = "unknown"
) {
    companion object {
        /**
         * Get current platform information.
         *
         * @return Platform info for the current environment.
         */
        fun current(): PlatformInfo {
            return PlatformInfo(
                isTerminalAnsiCompliant = detectAnsiSupport(),
                isMobileIdeConsole = detectMobileIde(),
                isCiCdEnvironment = detectCiCd(),
                os = detectOs()
            )
        }

        private fun detectAnsiSupport(): Boolean {
            val term = Platform.getenv("TERM")
            val ci = Platform.getenv("CI")
            return term != null || ci != null
        }

        private fun detectMobileIde(): Boolean {
            return Platform.getenv("ANDROID_ID") != null ||
                    Platform.getenv("XCODE_VERSION") != null
        }

        private fun detectCiCd(): Boolean {
            return Platform.getenv("CI") != null ||
                    Platform.getenv("GITHUB_ACTIONS") != null ||
                    Platform.getenv("GITLAB_CI") != null ||
                    Platform.getenv("JENKINS_URL") != null
        }

        private fun detectOs(): String {
            return Platform.getProperty("os.name") ?: "unknown"
        }
    }
}

/**
 * Adaptive log visual configuration that selects theme based on platform.
 */
internal class AdaptiveLogVisualConfig(
    private val platformProvider: () -> PlatformInfo,
    private val themeSelector: (PlatformInfo) -> LogVisualConfig
) : LogVisualConfig {
    private val delegate: LogVisualConfig by lazy {
        themeSelector(platformProvider())
    }

    override val indicator: LogLevelVisual
        get() = delegate.indicator

    override val labelFormatter: (LogLevel, String) -> String
        get() = delegate.labelFormatter

    override val timestampFormatter: ((Long) -> String)?
        get() = delegate.timestampFormatter
}
