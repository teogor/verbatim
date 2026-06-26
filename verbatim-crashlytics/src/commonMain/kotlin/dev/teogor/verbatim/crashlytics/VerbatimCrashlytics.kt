package dev.teogor.verbatim.crashlytics

import dev.teogor.verbatim.core.LogSink
import dev.teogor.verbatim.core.VerbatimPipeline

/**
 * Extension functions for installing [CrashSink] with [VerbatimPipeline].
 */
object VerbatimCrashlytics {

    /**
     * The default [CrashSink] instance managed by this object.
     */
    val sink: CrashSink = CrashSink()

    /**
     * Install the [CrashSink] with the given [engine].
     *
     * @param engine The crash reporting engine to use.
     * @param configure Optional additional configuration for the sink.
     */
    fun install(
        engine: CrashReportEngine,
        configure: CrashSink.() -> Unit = {}
    ) {
        sink.engine = engine
        sink.configure()
        VerbatimPipeline.addSink(sink)
    }

    /**
     * Uninstall the [CrashSink] from the logger pipeline.
     */
    fun uninstall() {
        VerbatimPipeline.removeSink(sink)
    }
}
