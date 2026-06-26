package dev.teogor.verbatim.core

import dev.teogor.verbatim.core.formatters.CompactLogFormatter
import dev.teogor.verbatim.core.formatters.DefaultLogFormatter
import dev.teogor.verbatim.core.formatters.JsonLogFormatter
import dev.teogor.verbatim.core.formatters.PrettyLogFormatter
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Factory for creating common [LogFormatter] instances.
 *
 * Example usage:
 * ```kotlin
 * val formatter = LogFormatters.default(LogVisuals.Emojis)
 * val jsonFormatter = LogFormatters.json()
 * ```
 */
object LogFormatters {
    /**
     * Creates a default formatter with visual configuration.
     *
     * @param visualConfig Visual configuration for level indicators and labels.
     * @return A [DefaultLogFormatter] instance.
     */
    fun default(visualConfig: LogVisualConfig = LogVisuals.Emojis): LogFormatter = 
        DefaultLogFormatter(visualConfig)

    /**
     * Creates a pretty formatter for development and debugging.
     *
     * @param visualConfig Visual configuration for level indicators and labels.
     * @param includeTimestamp Whether to include timestamps in the output.
     * @param includeThread Whether to include thread names in the output.
     * @return A [PrettyLogFormatter] instance.
     */
    fun pretty(
        visualConfig: LogVisualConfig = LogVisuals.Emojis,
        includeTimestamp: Boolean = true,
        includeThread: Boolean = true
    ): LogFormatter = PrettyLogFormatter(visualConfig, includeTimestamp, includeThread)

    /**
     * Creates a compact formatter for production use.
     *
     * @param visualConfig Visual configuration for level indicators and labels.
     * @return A [CompactLogFormatter] instance.
     */
    fun compact(visualConfig: LogVisualConfig = LogVisuals.Emojis): LogFormatter = 
        CompactLogFormatter(visualConfig)

    /**
     * Creates a JSON formatter for structured logging.
     *
     * @return A [JsonLogFormatter] instance.
     */
    fun json(): LogFormatter = JsonLogFormatter()
}
