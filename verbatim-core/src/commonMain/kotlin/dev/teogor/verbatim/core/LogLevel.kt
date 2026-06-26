package dev.teogor.verbatim.core

/**
 * Log levels for Verbatim, ordered from least to most severe via weights.
 *
 * @property weight A numerical priority index used for high-speed filter evaluations.
 *   Lower values indicate less severe levels.
 */
enum class LogLevel(val weight: Int) {
    /** Detailed information, typically only useful during development. */
    VERBOSE(1),
    /** Debugging information. */
    DEBUG(2),
    /** General operational information. */
    INFO(3),
    /** Potential issues or important but non-critical occurrences. */
    WARN(4),
    /** Errors and failures that should be investigated. */
    ERROR(5),
    /** Severe errors that will lead to application termination. */
    FATAL(6),
    /** Special level used to turn off all logging. Should never be used for log events. */
    OFF(Int.MAX_VALUE);

    /**
     * Helper evaluation logic for threshold filtration.
     *
     * @param other The log level to compare against.
     * @return true if this level is at least as severe as [other].
     */
    fun isAtLeast(other: LogLevel): Boolean = this.weight >= other.weight

    companion object {
        /**
         * Returns the [LogLevel] corresponding to the given [weight], or null if no match.
         *
         * @param weight The weight to look up.
         * @return The matching [LogLevel], or null.
         */
        fun fromWeight(weight: Int): LogLevel? = entries.find { it.weight == weight }
    }
}
