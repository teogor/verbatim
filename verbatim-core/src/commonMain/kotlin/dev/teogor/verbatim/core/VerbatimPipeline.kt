package dev.teogor.verbatim.core

/**
 * The main entry point for configuring the logging pipeline.
 *
 * This object manages the collection of [LogSink] instances that receive log events.
 * Sinks can be added or removed dynamically at runtime.
 */
object VerbatimPipeline {
    private val sinks = mutableListOf<LogSink>()

    /**
     * Adds a sink to the logging pipeline.
     *
     * @param sink The sink to add.
     */
    fun addSink(sink: LogSink) {
        sinks.add(sink)
    }

    /**
     * Removes a sink from the logging pipeline.
     *
     * @param sink The sink to remove.
     */
    fun removeSink(sink: LogSink) {
        sinks.remove(sink)
    }

    /**
     * Returns all registered sinks.
     */
    internal fun getSinks(): List<LogSink> = sinks
}
