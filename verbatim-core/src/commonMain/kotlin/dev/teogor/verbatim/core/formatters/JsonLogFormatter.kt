package dev.teogor.verbatim.core.formatters

import dev.teogor.verbatim.core.LogEvent
import dev.teogor.verbatim.core.LogFormatter

/**
 * JSON log formatter for structured logging and remote services.
 *
 * Produces JSON output suitable for log aggregation systems:
 * ```json
 * {"level":"INFO","logger":"MyApp","message":"User logged in","timestamp":1705312245123}
 * ```
 */
class JsonLogFormatter : LogFormatter {

    override fun format(event: LogEvent): String {
        return buildString {
            append("{")
            append("\"level\":\"${event.level.name}\",")
            append("\"levelWeight\":${event.level.weight},")
            append("\"logger\":\"${escapeJson(event.loggerName)}\",")
            append("\"message\":\"${escapeJson(event.message ?: "")}\",")
            append("\"timestamp\":${event.timestamp.toEpochMilliseconds()}")
            
            event.throwable?.let { throwable ->
                append(",\"throwable\":\"${escapeJson(throwable.message ?: "")}\"")
            }
            
            if (event.attributes.isNotEmpty()) {
                append(",\"attributes\":${toJson(event.attributes)}")
            }
            
            if (event.context.values.isNotEmpty()) {
                append(",\"context\":${toJson(event.context.values)}")
            }
            
            append("}")
        }
    }
    
    private fun escapeJson(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
    
    private fun toJson(map: Map<String, Any?>): String {
        return map.entries.joinToString(",", "{", "}") { (key, value) ->
            val jsonValue = when (value) {
                is String -> "\"${escapeJson(value)}\""
                is Number -> value.toString()
                is Boolean -> value.toString()
                else -> "\"${escapeJson(value.toString())}\""
            }
            "\"${escapeJson(key)}\":$jsonValue"
        }
    }
}
