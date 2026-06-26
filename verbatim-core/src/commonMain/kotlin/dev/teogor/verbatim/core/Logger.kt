package dev.teogor.verbatim.core

import kotlin.time.Clock
import kotlin.time.Instant

/**
 * A logger instance for a specific component.
 *
 * This class provides methods for logging at different levels. Log events are emitted
 * to all registered sinks via the [VerbatimPipeline].
 *
 * @property tag The tag identifying the component that created this logger.
 */
class Logger(val tag: String) {

    /**
     * Log a message at [LogLevel.VERBOSE].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun v(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.VERBOSE, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.VERBOSE].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun verbose(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.VERBOSE, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.DEBUG].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun d(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.DEBUG, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.DEBUG].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun debug(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.DEBUG, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.INFO].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun i(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.INFO, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.INFO].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun info(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.INFO, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.WARN].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun w(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.WARN, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.WARN].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun warn(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.WARN, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.ERROR].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun e(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.ERROR, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.ERROR].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun error(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.ERROR, throwable, attributes, message)
    }

    /**
     * Log a message at [LogLevel.FATAL].
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun fatal(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) {
        log(LogLevel.FATAL, throwable, attributes, message)
    }

    private fun log(
        level: LogLevel,
        throwable: Throwable?,
        attributes: LogAttributesBuilder.() -> Unit,
        message: () -> String
    ) {
        val event = LogEvent(
            level = level,
            loggerName = tag,
            message = message(),
            throwable = throwable,
            attributes = LogAttributesBuilder().apply(attributes).build(),
            context = LogContext(),
            thread = Platform.currentThreadName(),
            timestamp = kotlin.time.Clock.System.now()
        )
        
        LogPipeline.process(event)
    }
}
