package dev.teogor.verbatim.crashlytics

/**
 * Abstract interface for crash reporting engines.
 *
 * Implement this interface to integrate Verbatim with your preferred crash reporting tool
 * (Firebase Crashlytics, Sentry, Bugsnag, Datadog, etc.).
 *
 * Example implementation:
 * ```kotlin
 * class FirebaseCrashEngine : CrashReportEngine {
 *     override fun recordException(t: Throwable, attributes: Map<String, Any?>) {
 *         FirebaseCrashlytics.getInstance().recordException(t)
 *         attributes.forEach { (key, value) ->
 *             FirebaseCrashlytics.getInstance().setCustomKey(key, value?.toString() ?: "null")
 *         }
 *     }
 *
 *     override fun log(message: String, severity: CrashSeverity) {
 *         FirebaseCrashlytics.getInstance().log(message)
 *     }
 * }
 * ```
 */
interface CrashReportEngine {

    /**
     * Record an exception with associated attributes.
     *
     * @param t The exception to record.
     * @param attributes Key-value metadata attached to the exception.
     */
    fun recordException(t: Throwable, attributes: Map<String, Any?> = emptyMap())

    /**
     * Log a message to the crash reporting system.
     *
     * @param message The log message.
     * @param severity The severity level of the message.
     */
    fun log(message: String, severity: CrashSeverity = CrashSeverity.INFO)
}

/**
 * Severity levels for crash reporting.
 */
enum class CrashSeverity {
    /** Verbose/debug-level information. */
    DEBUG,
    /** General informational messages. */
    INFO,
    /** Potential issues or warnings. */
    WARNING,
    /** Errors that need investigation. */
    ERROR,
    /** Fatal/unrecoverable errors. */
    FATAL
}
