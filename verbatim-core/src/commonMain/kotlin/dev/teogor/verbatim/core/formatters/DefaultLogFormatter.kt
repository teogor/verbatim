package dev.teogor.verbatim.core.formatters

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Default log formatter that produces human-readable output.
 *
 * Format: `INDICATOR [LEVEL] tag: message`
 *
 * Example: `💙 [INFO] MyApp: User logged in successfully`
 *
 * @property visualConfig Visual configuration for level indicators and labels.
 */
class DefaultLogFormatter(
    private val visualConfig: LogVisualConfig = LogVisuals.Emojis
) : LogFormatter {

    override fun format(event: LogEvent): String {
        val indicator = visualConfig.indicator.getIndicator(event.level)
        val label = visualConfig.labelFormatter(event.level, event.loggerName)
        val base = if (indicator.isNotEmpty()) {
            "$indicator [$label]: ${event.message}"
        } else {
            "[$label]: ${event.message}"
        }
        
        return buildString {
            append(base)
            
            event.throwable?.let { throwable ->
                append("\n  Exception: ${throwable.message}")
            }
            
            if (event.attributes.isNotEmpty()) {
                append("\n  Attributes: ${event.attributes}")
            }
            
            if (event.context.values.isNotEmpty()) {
                append("\n  Context: ${event.context.values}")
            }
        }
    }
}
