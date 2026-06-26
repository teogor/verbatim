package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.formatters.DefaultLogFormatter
import dev.teogor.verbatim.core.sinks.ConsoleSink
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Configuration for the logging pipeline.
 *
 * This class holds all configuration for the logging system including:
 * - Minimum log level
 * - Global attributes
 * - Registered sinks
 * - Per-tag level overrides
 * - Visual configuration for formatters
 *
 * Use the [Builder] to create instances:
 * ```kotlin
 * val config = LoggerConfig.Builder()
 *     .minLevel(LogLevel.DEBUG)
 *     .visuals(LogVisuals.Geometric)
 *     .addSink(DefaultLogSink())
 *     .addSink(ConsoleSink())
 *     .override("NetworkModule", LogLevel.VERBOSE)
 *     .build()
 * ```
 *
 * @property minimumLogLevel The minimum log level for all loggers.
 * @property globalAttributes Attributes to include in all log events.
 * @property sinks The list of registered log sinks.
 * @property overrides Per-tag log level overrides.
 * @property visuals Visual configuration for log formatters.
 */
class LoggerConfig private constructor(
    val minimumLogLevel: LogLevel,
    val globalAttributes: Map<String, Any?>,
    val sinks: List<LogSink>,
    val overrides: Map<String, LogLevel>,
    val visuals: LogVisualConfig
) {
    /**
     * A builder for creating [LoggerConfig] instances.
     *
     * Example usage:
     * ```kotlin
     * val config = LoggerConfig.Builder()
     *     .minLevel(LogLevel.DEBUG)
     *     .visuals(LogVisuals.Geometric)
     *     .globalAttribute("app_version", "1.0.0")
     *     .addSink(DefaultLogSink())
     *     .build()
     * ```
     */
    class Builder {
        private var minLevel = LogLevel.INFO
        private val globalAttributes = mutableMapOf<String, Any?>()
        private val sinks = mutableListOf<LogSink>()
        private val overrides = mutableMapOf<String, LogLevel>()
        private var visuals: LogVisualConfig = LogVisuals.Emojis
        
        /**
         * Set the minimum log level.
         *
         * @param level The minimum level for log events.
         * @return This builder for chaining.
         */
        fun minLevel(level: LogLevel) = apply { minLevel = level }
        
        /**
         * Set the visual configuration for log formatters.
         *
         * @param config The visual configuration to use.
         * @return This builder for chaining.
         */
        fun visuals(config: LogVisualConfig) = apply { visuals = config }
        
        /**
         * Add a global attribute to be included in all log events.
         *
         * @param key The attribute key.
         * @param value The attribute value.
         * @return This builder for chaining.
         */
        fun globalAttribute(key: String, value: Any?) = apply { globalAttributes[key] = value }
        
        /**
         * Add a log sink to the pipeline.
         *
         * @param sink The sink to add.
         * @return This builder for chaining.
         */
        fun addSink(sink: LogSink) = apply { sinks.add(sink) }
        
        /**
         * Override the log level for a specific tag.
         *
         * @param tag The logger tag.
         * @param level The minimum log level for this tag.
         * @return This builder for chaining.
         */
        fun override(tag: String, level: LogLevel) = apply { overrides[tag] = level }
        
        /**
         * Build the configuration.
         *
         * @return A new [LoggerConfig] instance.
         * @throws IllegalArgumentException if no sinks are configured.
         */
        fun build(): LoggerConfig {
            val effectiveSinks = if (sinks.isEmpty()) {
                listOf(ConsoleSink(DefaultLogFormatter(visuals)))
            } else {
                sinks.toList()
            }
            return LoggerConfig(
                minimumLogLevel = minLevel,
                globalAttributes = globalAttributes.toMap(),
                sinks = effectiveSinks,
                overrides = overrides.toMap(),
                visuals = visuals
            )
        }
    }
}
