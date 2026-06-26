package dev.teogor.verbatim.core.visuals

import dev.teogor.verbatim.core.LogLevel

/**
 * Complete visual configuration for log formatting.
 *
 * This interface holds all visual customization options for log output:
 * - Level indicators (emojis, icons, colors)
 * - Label formatting
 * - Timestamp formatting
 *
 * Example usage:
 * ```kotlin
 * val config = LogVisualConfig(
 *     indicator = LogLevelVisual { level -> "💚" },
 *     labelFormatter = { level, tag -> "[${level.name}] $tag" }
 * )
 * ```
 */
interface LogVisualConfig {
    /** Provides visual indicators for each log level. */
    val indicator: LogLevelVisual

    /** Formats the level label and tag. */
    val labelFormatter: (LogLevel, String) -> String

    /** Optional formatter for timestamps. */
    val timestampFormatter: ((Long) -> String)?
}

/**
 * Default implementation of [LogVisualConfig].
 *
 * @property indicator Provides visual indicators for each log level.
 * @property labelFormatter Formats the level label and tag.
 * @property timestampFormatter Optional formatter for timestamps.
 */
data class DefaultLogVisualConfig(
    override val indicator: LogLevelVisual,
    override val labelFormatter: (LogLevel, String) -> String = { level, tag -> tag },
    override val timestampFormatter: ((Long) -> String)? = null
) : LogVisualConfig
