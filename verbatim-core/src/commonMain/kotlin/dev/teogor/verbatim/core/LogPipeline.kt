package dev.teogor.verbatim.core

/**
 * The log pipeline that manages log level filtering and event processing.
 *
 * This object provides:
 * - Global minimum log level configuration
 * - Per-tag level overrides
 * - Event processing with level filtering
 * - Special handling for FATAL level (flush + throw)
 *
 * Example usage:
 * ```kotlin
 * LogPipeline.minimumLogLevel = LogLevel.DEBUG
 * LogPipeline.override("NetworkModule", LogLevel.VERBOSE)
 * ```
 */
object LogPipeline {
    /**
     * The minimum log level for all loggers.
     * Events below this level will be filtered out.
     */
    var minimumLogLevel: LogLevel = LogLevel.INFO
    
    private val overrides = mutableMapOf<String, LogLevel>()
    
    /**
     * Override the log level for a specific tag.
     *
     * @param tag The logger tag.
     * @param level The minimum log level for this tag.
     */
    fun override(tag: String, level: LogLevel) {
        overrides[tag] = level
    }
    
    /**
     * Remove a level override for a specific tag.
     *
     * @param tag The logger tag.
     */
    fun removeOverride(tag: String) {
        overrides.remove(tag)
    }
    
    /**
     * Get the effective log level for a specific tag.
     *
     * @param tag The logger tag.
     * @return The effective minimum log level.
     */
    fun getLevelForTag(tag: String): LogLevel {
        return overrides[tag] ?: minimumLogLevel
    }
    
    /**
     * Process a log event through the pipeline.
     *
     * This method:
     * 1. Checks if the event level meets the effective minimum level
     * 2. If FATAL, flushes all sinks before and after emission
     * 3. Emits the event to all registered sinks
     *
     * @param event The log event to process.
     * @return true if the event was emitted, false if filtered out.
     */
    fun process(event: LogEvent): Boolean {
        // OFF is a configuration sentinel, never a log event level
        if (event.level == LogLevel.OFF) {
            return false
        }
        
        val effectiveLevel = getLevelForTag(event.loggerName)
        
        // When minimum is OFF, block all events
        if (effectiveLevel == LogLevel.OFF) {
            return false
        }
        
        // Filter out events below the effective level using weight
        if (!event.level.isAtLeast(effectiveLevel)) {
            return false
        }
        
        // Special handling for FATAL: flush before and after
        if (event.level == LogLevel.FATAL) {
            VerbatimPipeline.getSinks().forEach { it.flush() }
            VerbatimPipeline.getSinks().forEach { it.emit(event) }
            VerbatimPipeline.getSinks().forEach { it.flush() }
            return true
        }
        
        // Regular emission
        VerbatimPipeline.getSinks().forEach { it.emit(event) }
        return true
    }
    
    /**
     * Clear all level overrides.
     */
    fun clearOverrides() {
        overrides.clear()
    }
}
