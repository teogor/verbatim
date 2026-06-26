package dev.teogor.verbatim.core

/**
 * A logger wrapper that provides a fixed tag for all log messages.
 *
 * This class wraps a [Logger] and provides convenience methods for logging
 * with a pre-configured tag.
 *
 * Example usage:
 * ```kotlin
 * val logger = TaggedLogger("MyComponent")
 * logger.info { "Component initialized" }
 * logger.error(exception) { "Operation failed" }
 * ```
 *
 * @property tag The tag for all log messages from this logger.
 */
class TaggedLogger(private val tag: String) {
    private val logger = Logger(tag)

    /**
     * Log a message at [LogLevel.VERBOSE].
     *
     * @param message Lazy message provider.
     */
    fun v(message: () -> String) = logger.v(message = message)

    /**
     * Log a message at [LogLevel.DEBUG].
     *
     * @param message Lazy message provider.
     */
    fun d(message: () -> String) = logger.d(message = message)

    /**
     * Log a message at [LogLevel.INFO].
     *
     * @param message Lazy message provider.
     */
    fun i(message: () -> String) = logger.i(message = message)

    /**
     * Log a message at [LogLevel.WARN].
     *
     * @param message Lazy message provider.
     */
    fun w(message: () -> String) = logger.w(message = message)

    /**
     * Log a message at [LogLevel.ERROR].
     *
     * @param message Lazy message provider.
     */
    fun e(message: () -> String) = logger.e(message = message)

    /**
     * Log a message at [LogLevel.FATAL].
     *
     * @param message Lazy message provider.
     */
    fun fatal(message: () -> String) = logger.fatal(message = message)

    /**
     * Log a message at [LogLevel.VERBOSE] with an exception.
     *
     * @param throwable Optional exception to log.
     * @param message Lazy message provider.
     */
    fun v(throwable: Throwable? = null, message: () -> String) = 
        logger.v(throwable, message = message)

    /**
     * Log a message at [LogLevel.DEBUG] with an exception.
     *
     * @param throwable Optional exception to log.
     * @param message Lazy message provider.
     */
    fun d(throwable: Throwable? = null, message: () -> String) = 
        logger.d(throwable, message = message)

    /**
     * Log a message at [LogLevel.INFO] with an exception.
     *
     * @param throwable Optional exception to log.
     * @param message Lazy message provider.
     */
    fun i(throwable: Throwable? = null, message: () -> String) = 
        logger.i(throwable, message = message)

    /**
     * Log a message at [LogLevel.WARN] with an exception.
     *
     * @param throwable Optional exception to log.
     * @param message Lazy message provider.
     */
    fun w(throwable: Throwable? = null, message: () -> String) = 
        logger.w(throwable, message = message)

    /**
     * Log a message at [LogLevel.ERROR] with an exception.
     *
     * @param throwable Optional exception to log.
     * @param message Lazy message provider.
     */
    fun e(throwable: Throwable? = null, message: () -> String) = 
        logger.e(throwable, message = message)

    /**
     * Log a message at [LogLevel.FATAL] with an exception.
     *
     * @param throwable Optional exception to log.
     * @param message Lazy message provider.
     */
    fun fatal(throwable: Throwable? = null, message: () -> String) = 
        logger.fatal(throwable, message = message)

    /**
     * Log a message at [LogLevel.VERBOSE] with attributes.
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun v(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = logger.v(throwable, attributes, message)

    /**
     * Log a message at [LogLevel.DEBUG] with attributes.
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun d(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = logger.d(throwable, attributes, message)

    /**
     * Log a message at [LogLevel.INFO] with attributes.
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun i(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = logger.i(throwable, attributes, message)

    /**
     * Log a message at [LogLevel.WARN] with attributes.
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun w(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = logger.w(throwable, attributes, message)

    /**
     * Log a message at [LogLevel.ERROR] with attributes.
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun e(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = logger.e(throwable, attributes, message)

    /**
     * Log a message at [LogLevel.FATAL] with attributes.
     *
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun fatal(
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = logger.fatal(throwable, attributes, message)
}
