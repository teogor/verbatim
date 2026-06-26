package dev.teogor.verbatim.core.visuals

import dev.teogor.verbatim.core.LogLevel

/**
 * DSL builder for creating custom [LogVisualConfig].
 *
 * Example usage:
 * ```kotlin
 * val customVisual = LogVisualsBuilder().apply {
 *     on(VERBOSE) { indicator = "🔍"; label = "VRB" }
 *     on(DEBUG)   { indicator = "🛠️"; label = "DBG" }
 *     on(INFO)    { indicator = "💡"; label = "INF" }
 *     on(WARN)    { indicator = "⚠️"; label = "WRN" }
 *     on(ERROR)   { indicator = "🚨"; label = "ERR" }
 *     on(FATAL)   { indicator = "💀"; label = "FTL" }
 * }.build()
 * ```
 */
class LogVisualsBuilder {
    private val indicators = mutableMapOf<LogLevel, String>()
    private val labels = mutableMapOf<LogLevel, String>()

    /**
     * Configure the visual representation for a specific log level.
     *
     * @param level The log level to configure.
     * @param block Configuration block for the level.
     */
    fun on(level: LogLevel, block: VisualLevelBuilder.() -> Unit) {
        val builder = VisualLevelBuilder(level)
        builder.block()
        indicators[level] = builder.indicator
        labels[level] = builder.label
    }

    /**
     * Build the [LogVisualConfig] from this builder.
     *
     * @return A new [LogVisualConfig] instance.
     */
    fun build(): LogVisualConfig {
        return DefaultLogVisualConfig(
            indicator = LogLevelVisual { level ->
                indicators[level] ?: level.name
            },
            labelFormatter = { level, tag ->
                val label = labels[level] ?: level.name
                "$label $tag"
            }
        )
    }
}

/**
 * Builder for configuring a single log level's visual representation.
 *
 * @property level The log level being configured.
 */
class VisualLevelBuilder internal constructor(private val level: LogLevel) {
    /**
     * The visual indicator for this level (e.g., emoji, icon).
     */
    var indicator: String = ""

    /**
     * The text label for this level (e.g., "INFO", "ERR").
     */
    var label: String = level.name
}

/**
 * Create a custom [LogVisualConfig] using a DSL.
 *
 * Example usage:
 * ```kotlin
 * val customVisual = customVisuals {
 *     on(VERBOSE) { indicator = "🔍"; label = "VRB" }
 *     on(DEBUG)   { indicator = "🛠️"; label = "DBG" }
 *     on(INFO)    { indicator = "💡"; label = "INF" }
 *     on(WARN)    { indicator = "⚠️"; label = "WRN" }
 *     on(ERROR)   { indicator = "🚨"; label = "ERR" }
 *     on(FATAL)   { indicator = "💀"; label = "FTL" }
 * }
 * ```
 *
 * @param block Configuration block.
 * @return A new [LogVisualConfig] instance.
 */
fun customVisuals(block: LogVisualsBuilder.() -> Unit): LogVisualConfig {
    return LogVisualsBuilder().apply(block).build()
}
