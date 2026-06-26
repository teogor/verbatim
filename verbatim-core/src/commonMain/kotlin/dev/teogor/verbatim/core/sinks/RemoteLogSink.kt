package dev.teogor.verbatim.core.sinks

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter
import dev.teogor.verbatim.core.LogFormatters
import dev.teogor.verbatim.core.LogSink

/**
 * Remote log sink for sending logs to external services.
 *
 * This sink formats log events and sends them to a remote service via a lambda.
 *
 * Example usage:
 * ```kotlin
 * VerbatimPipeline.addSink(
 *     RemoteLogSink(LogFormatters.json()) { payload ->
 *         httpClient.post("/logs") {
 *             body = payload
 *         }
 *     }
 * )
 * ```
 *
 * @property logFormatter The formatter to use for output.
 * @property send A lambda that receives the formatted log string and sends it.
 */
class RemoteLogSink(
    private val logFormatter: LogFormatter = LogFormatters.json(),
    private val send: (String) -> Unit
) : LogSink {

    /**
     * Emit a log event by formatting it and sending it to the remote service.
     *
     * @param event The log event to emit.
     */
    override fun emit(event: LogEvent) {
        val formatted = logFormatter.format(event)
        send(formatted)
    }

    /**
     * Flush any buffered log events.
     */
    override fun flush() {
        // No buffering in remote implementation
    }
}
