package dev.teogor.verbatim.coroutines

import dev.teogor.verbatim.core.LogContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * A [CoroutineContext.Element] that carries [LogContext] for structured logging in coroutines.
 *
 * This element allows log context to be propagated through coroutine hierarchies.
 *
 * Example usage:
 * ```kotlin
 * val element = LogContextElement(LogContext(mapOf("request_id" to "12345")))
 * 
 * // Use with coroutine context
 * with(element + coroutineContext) {
 *     // All log events within this block will include the context
 *     logger.info { "Processing request" }
 * }
 * ```
 *
 * @property context The log context to propagate.
 */
class LogContextElement(
    val context: LogContext
) : AbstractCoroutineContextElement(Key) {
    /**
     * Companion object implementing [CoroutineContext.Key] for [LogContextElement].
     */
    companion object Key : CoroutineContext.Key<LogContextElement>
}

/**
 * Get the [LogContext] from the current coroutine context.
 *
 * This is a convenience function to extract the log context from a coroutine context.
 *
 * @return The [LogContext] if present, or an empty [LogContext].
 */
suspend fun currentLogContext(): LogContext {
    return kotlin.coroutines.coroutineContext[LogContextElement]?.context ?: LogContext()
}

/**
 * Execute a block with a [LogContext] available in the coroutine context.
 *
 * This function creates a [LogContextElement] and adds it to the current coroutine context.
 *
 * Example usage:
 * ```kotlin
 * withLogContext(LogContext(mapOf("user_id" to "12345"))) {
 *     logger.info { "User logged in" } // Will include user_id in context
 *     suspendFunction() // Context propagates through suspension
 * }
 * ```
 *
 * @param context The log context to propagate.
 * @param block The block to execute with the log context.
 * @return The result of the block.
 */
suspend fun <T> withLogContext(
    context: LogContext,
    block: suspend () -> T
): T {
    val element = LogContextElement(context)
    val newContext = kotlin.coroutines.coroutineContext + element
    
    // Use kotlinx.coroutines.withContext if available, otherwise use a simple approach
    return kotlinx.coroutines.withContext(newContext) {
        block()
    }
}

/**
 * Execute a block with a log context built from key-value pairs.
 *
 * This is a convenience overload that creates a [LogContext] from the provided pairs.
 *
 * Example usage:
 * ```kotlin
 * withLogContext("request_id" to "12345", "user_id" to "67890") {
 *     logger.info { "Processing request" }
 * }
 * ```
 *
 * @param pairs Key-value pairs to include in the log context.
 * @param block The block to execute with the log context.
 * @return The result of the block.
 */
suspend fun <T> withLogContext(
    vararg pairs: Pair<String, Any?>,
    block: suspend () -> T
): T {
    val context = LogContext(pairs.toMap())
    return withLogContext(context, block)
}
