package dev.teogor.verbatim.core

/**
 * A builder for creating structured log attributes.
 *
 * This class provides a DSL for building key-value pairs that can be attached to log events.
 *
 * Example usage:
 * ```kotlin
 * val attributes = buildAttributes {
 *     attr("user_id", "12345")
 *     attr("request_count", 3)
 *     attr("is_retry", true)
 * }
 * ```
 */
class LogAttributesBuilder {
    private val attributes = mutableMapOf<String, Any?>()

    /**
     * Add a string attribute.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun attr(key: String, value: String?) {
        attributes[key] = value
    }

    /**
     * Add an integer attribute.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun attr(key: String, value: Int) {
        attributes[key] = value
    }

    /**
     * Add a long attribute.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun attr(key: String, value: Long) {
        attributes[key] = value
    }

    /**
     * Add a double attribute.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun attr(key: String, value: Double) {
        attributes[key] = value
    }

    /**
     * Add a boolean attribute.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun attr(key: String, value: Boolean) {
        attributes[key] = value
    }

    /**
     * Add any attribute value.
     *
     * @param key The attribute key.
     * @param value The attribute value.
     */
    fun attr(key: String, value: Any?) {
        attributes[key] = value
    }

    /**
     * Build the final attributes map.
     *
     * @return An immutable map of attributes.
     */
    fun build(): Map<String, Any?> = attributes.toMap()
}

/**
 * Build a map of log attributes using a DSL.
 *
 * Example usage:
 * ```kotlin
 * val attributes = buildAttributes {
 *     attr("user_id", "12345")
 *     attr("request_count", 3)
 * }
 * ```
 *
 * @param block The configuration block.
 * @return An immutable map of attributes.
 */
fun buildAttributes(block: LogAttributesBuilder.() -> Unit): Map<String, Any?> =
    LogAttributesBuilder().apply(block).build()
