package dev.teogor.verbatim.crashlytics

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LogSink

/**
 * A [LogSink] that bridges log events to a [CrashReportEngine].
 *
 * This sink forwards ERROR and FATAL level logs as exceptions,
 * and all other levels as regular log messages.
 *
 * Usage:
 * ```kotlin
 * Verbatim.initialize {
 *     install(CrashSink) {
 *         engine = MyCrashEngine()
 *         minLevel = LogLevel.ERROR
 *     }
 * }
 * ```
 */
class CrashSink : LogSink {

    private var _engine: CrashReportEngine? = null

    /**
     * The crash reporting engine to forward events to.
     */
    var engine: CrashReportEngine
        get() = _engine ?: error("CrashReportEngine not set. Call `engine = YourEngine()` in the install block.")
        set(value) { _engine = value }

    /**
     * Minimum log level to forward to the crash reporter.
     * Logs below this level are silently dropped.
     */
    var minLevel: LogLevel = LogLevel.ERROR

    override fun emit(event: LogEvent) {
        val engine = _engine ?: return

        if (event.level.ordinal < minLevel.ordinal) return

        val crashLevel = event.level.toCrashLevel()

        val throwable = event.throwable
        if (throwable != null) {
            val attributes = event.attributes + event.context.values
            engine.recordException(throwable, attributes)
        } else {
            val message = formatMessage(event)
            engine.log(message, crashLevel)
        }
    }

    override fun flush() {
        // No-op: crash reporters typically flush internally
    }

    private fun formatMessage(event: LogEvent): String {
        val sb = StringBuilder()
        sb.append("[${event.level.name}] ")
        sb.append("${event.loggerName}: ")
        sb.append(event.message ?: "")
        if (event.attributes.isNotEmpty()) {
            sb.append(" | attrs=${event.attributes}")
        }
        if (event.context.values.isNotEmpty()) {
            sb.append(" | ctx=${event.context.values}")
        }
        return sb.toString()
    }

    private fun LogLevel.toCrashLevel(): CrashSeverity = when (this) {
        LogLevel.VERBOSE, LogLevel.DEBUG -> CrashSeverity.DEBUG
        LogLevel.INFO -> CrashSeverity.INFO
        LogLevel.WARN -> CrashSeverity.WARNING
        LogLevel.ERROR -> CrashSeverity.ERROR
        LogLevel.FATAL -> CrashSeverity.FATAL
        LogLevel.OFF -> CrashSeverity.FATAL
    }
}
