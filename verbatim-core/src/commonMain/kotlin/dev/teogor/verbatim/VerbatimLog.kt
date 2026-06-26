package dev.teogor.verbatim

import dev.teogor.verbatim.core.LogAttributesBuilder
import dev.teogor.verbatim.core.Logger
import kotlin.reflect.KClass

/**
 * Simple logging API for quick and easy logging.
 *
 * This object provides static methods for logging without explicitly creating a [Logger] instance.
 *
 * Example usage:
 * ```kotlin
 * // Simple logging with default tag
 * VerbatimLog.d { "Debug message" }
 * VerbatimLog.i { "Info message" }
 *
 * // Logging with custom tag
 * VerbatimLog.d("Network") { "Request sent" }
 *
 * // Creating tagged loggers
 * val logger = VerbatimLog.withTag("MyComponent")
 * logger.info { "Component initialized" }
 * ```
 */
object VerbatimLog {
    private var defaultTag: String = "Verbatim"
    
    /**
     * Set the default tag for log messages.
     *
     * @param tag The default tag to use.
     */
    fun setDefaultTag(tag: String) {
        defaultTag = tag
    }

    /**
     * Create a logger with a specific tag.
     *
     * @param tag The tag for the logger.
     * @return A new [Logger] instance.
     */
    fun withTag(tag: String): Logger = Logger(tag)

    /**
     * Create a logger with a class name as the tag.
     *
     * @param clazz The class to use as the tag.
     * @return A new [Logger] instance.
     */
    fun <T : Any> withClassTag(clazz: KClass<T>): Logger = 
        Logger(clazz.simpleName ?: "Unknown")

    /**
     * Create a logger with a reified class name as the tag.
     *
     * @return A new [Logger] instance.
     */
    inline fun <reified T : Any> withClassTag(): Logger = withClassTag(T::class)

    /**
     * Log a message at [dev.teogor.verbatim.core.LogLevel.VERBOSE].
     *
     * @param tag Optional custom tag.
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun v(
        tag: String = defaultTag,
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = Logger(tag).v(throwable, attributes, message)

    /**
     * Log a message at [dev.teogor.verbatim.core.LogLevel.DEBUG].
     *
     * @param tag Optional custom tag.
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun d(
        tag: String = defaultTag,
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = Logger(tag).d(throwable, attributes, message)

    /**
     * Log a message at [dev.teogor.verbatim.core.LogLevel.INFO].
     *
     * @param tag Optional custom tag.
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun i(
        tag: String = defaultTag,
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = Logger(tag).i(throwable, attributes, message)

    /**
     * Log a message at [dev.teogor.verbatim.core.LogLevel.WARN].
     *
     * @param tag Optional custom tag.
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun w(
        tag: String = defaultTag,
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = Logger(tag).w(throwable, attributes, message)

    /**
     * Log a message at [dev.teogor.verbatim.core.LogLevel.ERROR].
     *
     * @param tag Optional custom tag.
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun e(
        tag: String = defaultTag,
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = Logger(tag).e(throwable, attributes, message)

    /**
     * Log a message at [dev.teogor.verbatim.core.LogLevel.FATAL].
     *
     * @param tag Optional custom tag.
     * @param throwable Optional exception to log.
     * @param attributes Optional structured attributes.
     * @param message Lazy message provider.
     */
    fun fatal(
        tag: String = defaultTag,
        throwable: Throwable? = null,
        attributes: LogAttributesBuilder.() -> Unit = {},
        message: () -> String
    ) = Logger(tag).fatal(throwable, attributes, message)
}
