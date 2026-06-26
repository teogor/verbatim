package dev.teogor.verbatim.core.formatters

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.visuals.LogVisualConfig
import dev.teogor.verbatim.core.visuals.LogVisuals

/**
 * Compact log formatter for production use.
 *
 * Produces minimal output: `INDICATOR LEVEL tag: message`
 *
 * Example: `💙 INFO MyApp: User logged in`
 *
 * @property visualConfig Visual configuration for level indicators and labels.
 */
class CompactLogFormatter(
    private val visualConfig: LogVisualConfig = LogVisuals.Emojis
) : LogFormatter {

    override fun format(event: LogEvent): String {
        val indicator = visualConfig.indicator.getIndicator(event.level)
        val label = visualConfig.labelFormatter(event.level, event.loggerName)
        
        return if (indicator.isNotEmpty()) {
            "$indicator $label: ${event.message}"
        } else {
            "$label: ${event.message}"
        }
    }
}
