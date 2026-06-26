package dev.teogor.verbatim.core.sinks

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter
import dev.teogor.verbatim.core.LogFormatters
import dev.teogor.verbatim.core.LogSink
import dev.teogor.verbatim.core.Platform

/**
 * Default log sink that outputs to the platform's native logging system.
 *
 * This sink uses a configurable [LogFormatter] to format log events before output,
 * then routes them to the appropriate native logging API via [Platform.platformLog]:
 *
 * - Android: `android.util.Log` (Logcat)
 * - iOS/macOS/tvOS/watchOS: `NSLog`
 * - JVM: `java.util.logging.Logger`
 * - JS/WasmJS: `console.log/info/warn/error`
 * - Linux/Windows: `println` (stdout)
 *
 * Example usage:
 * ```kotlin
 * VerbatimPipeline.addSink(DefaultLogSink())
 * VerbatimPipeline.addSink(DefaultLogSink(LogFormatters.json()))
 * ```
 *
 * @property logFormatter The formatter to use for output.
 */
class DefaultLogSink(
    private val logFormatter: LogFormatter = LogFormatters.default()
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
        // No buffering in default implementation
    }
}
