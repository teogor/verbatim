package dev.teogor.verbatim.core.sinks

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter
import dev.teogor.verbatim.core.LogFormatters
import dev.teogor.verbatim.core.LogSink
import dev.teogor.verbatim.core.Platform
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Console log sink for development and debugging.
 *
 * This sink outputs log events to the platform's native logging system
 * with configurable visual themes. Delegates to [Platform.platformLog]
 * for platform-aware output.
 *
 * Example usage:
 * ```kotlin
 * VerbatimPipeline.addSink(ConsoleSink())
 * VerbatimPipeline.addSink(ConsoleSink(LogFormatters.pretty(LogVisuals.Geometric)))
 * ```
 *
 * @property logFormatter The formatter to use for output.
 */
class ConsoleSink(
    private val logFormatter: LogFormatter = LogFormatters.default(LogVisuals.Emojis)
) : LogSink {

    /**
     * Emit a log event to the platform's native logging system.
     *
     * @param event The log event to emit.
     */
    override fun emit(event: LogEvent) {
        val formatted = logFormatter.format(event)
        Platform.platformLog(event.level, event.loggerName, formatted, event.throwable)
    }

    /**
     * Flush any buffered log events.
     */
    override fun flush() {
        // No buffering in console implementation
    }
}
