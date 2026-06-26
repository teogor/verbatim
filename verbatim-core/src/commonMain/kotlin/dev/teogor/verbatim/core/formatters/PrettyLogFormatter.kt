package dev.teogor.verbatim.core.formatters

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Pretty log formatter for development and debugging.
 *
 * Produces multi-line output with detailed information:
 * ```
 * 💙 [INFO] 2024-01-15 10:30:45.123 [main] MyApp: User logged in
 *   Attributes: {user_id=12345, session=abc}
 *   Context: {request_id=req-789}
 * ```
 *
 * @property visualConfig Visual configuration for level indicators and labels.
 * @property includeTimestamp Whether to include timestamps in the output.
 * @property includeThread Whether to include thread names in the output.
 */
class PrettyLogFormatter(
    private val visualConfig: LogVisualConfig = LogVisuals.Emojis,
    private val includeTimestamp: Boolean = true,
    private val includeThread: Boolean = true
) : LogFormatter {

    override fun format(event: LogEvent): String {
        val indicator = visualConfig.indicator.getIndicator(event.level)
        val label = visualConfig.labelFormatter(event.level, event.loggerName)
        
        return buildString {
            // Level indicator and label
            if (indicator.isNotEmpty()) {
                append("$indicator ")
            }
            append("[$label] ")
            
            // Timestamp
            if (includeTimestamp) {
                append("${formatTimestamp(event.timestamp)} ")
            }
            
            // Thread
            if (includeThread) {
                append("[${event.thread}] ")
            }
            
            // Message
            append(event.message)
            
            // Throwable
            event.throwable?.let { throwable ->
                append("\n  ↳ ${throwable::class.simpleName}: ${throwable.message}")
            }
            
            // Attributes
            if (event.attributes.isNotEmpty()) {
                append("\n  Attributes: ${formatMap(event.attributes)}")
            }
            
            // Context
            if (event.context.values.isNotEmpty()) {
                append("\n  Context: ${formatMap(event.context.values)}")
            }
        }
    }
    
    private fun formatTimestamp(timestamp: kotlin.time.Instant): String {
        return timestamp.toString()
    }
    
    private fun formatMap(map: Map<String, Any?>): String {
        return map.entries.joinToString(", ", "{", "}") { (key, value) ->
            "$key=$value"
        }
    }
}
