package dev.teogor.verbatim.core

/**
 * Platform-specific utilities for the Verbatim logging library.
 */
expect object Platform {
    /**
     * Get an environment variable by name.
     *
     * @param name The environment variable name.
     * @return The variable value, or null if not set.
     */
    fun getenv(name: String): String?

    /**
     * Get a system property by name.
     *
     * @param name The property name.
     * @return The property value, or null if not set.
     */
    fun getProperty(name: String): String?

    /**
     * Returns the name of the current thread.
     *
     * @return Current thread name.
     */
    fun currentThreadName(): String

    /**
     * Log a message to the platform's native logging system.
     *
     * - Android: `android.util.Log` (Logcat)
     * - iOS/macOS/tvOS/watchOS: `NSLog`
     * - JVM: `java.util.logging.Logger`
     * - JS/WasmJS: `console.log/info/warn/error`
     * - Linux/Windows: `println` (stdout)
     *
     * @param level The log level.
     * @param tag The logger tag.
     * @param message The log message.
     * @param throwable Optional throwable to log.
     */
    fun platformLog(level: LogLevel, tag: String, message: String, throwable: Throwable? = null)
}
