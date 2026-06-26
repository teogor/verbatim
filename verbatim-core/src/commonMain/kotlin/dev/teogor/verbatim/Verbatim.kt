package dev.teogor.verbatim

import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LogPipeline
import dev.teogor.verbatim.core.Logger
import dev.teogor.verbatim.core.LoggerConfig
import dev.teogor.verbatim.core.VerbatimPipeline
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Entry point for the Verbatim logging library.
 *
 * This object provides methods for initializing and configuring the logging system.
 *
 * Example usage:
 * ```kotlin
 * Verbatim.install(
 *     LoggerConfig.Builder()
 *         .minLevel(LogLevel.DEBUG)
 *         .addSink(DefaultLogSink())
 *         .build()
 * )
 * ```
 */
object Verbatim {
    private var installed = false

    inline fun initialize(block: LoggerConfig.Builder.() -> Unit) {
        val builder = LoggerConfig.Builder()
        builder.block()
        install(builder.build())
    }

    /**
     * Install a logging configuration.
     *
     * This method configures the logging pipeline with the provided [LoggerConfig].
     * It sets up the minimum log level, per-tag overrides, and registers all sinks.
     *
     * If already installed, this method will uninstall the previous configuration first.
     *
     * @param config The logging configuration to install.
     */
    fun install(config: LoggerConfig) {
        if (installed) {
            uninstall()
        }
        
        // Configure the pipeline
        LogPipeline.minimumLogLevel = config.minimumLogLevel
        config.overrides.forEach { (tag, level) ->
            LogPipeline.override(tag, level)
        }
        
        // Register sinks
        config.sinks.forEach { sink ->
            VerbatimPipeline.addSink(sink)
        }
        
        installed = true
    }

    /**
     * Uninstall the current logging configuration.
     *
     * This method removes all sinks and resets the pipeline configuration.
     * Safe to call even if not installed.
     */
    fun uninstall() {
        VerbatimPipeline.getSinks().toList().forEach { sink ->
            VerbatimPipeline.removeSink(sink)
        }
        LogPipeline.clearOverrides()
        LogPipeline.minimumLogLevel = LogLevel.INFO
        installed = false
    }

    /**
     * Install with a visual theme and default console sink.
     *
     * Convenience method that creates a default configuration with the specified
     * visual theme and a console sink.
     *
     * @param visuals The visual configuration for log formatting.
     * @param minLevel The minimum log level (default: INFO).
     */
    fun install(
        visuals: LogVisualConfig = LogVisuals.Emojis,
        minLevel: LogLevel = LogLevel.INFO
    ) {
        install(
            LoggerConfig.Builder()
                .minLevel(minLevel)
                .visuals(visuals)
                .build()
        )
    }

    /**
     * Returns a logger for the given [tag].
     *
     * @param tag The tag identifying the component.
     * @return A new [Logger] instance.
     */
    fun logger(tag: String): Logger {
        return Logger(tag)
    }
}
