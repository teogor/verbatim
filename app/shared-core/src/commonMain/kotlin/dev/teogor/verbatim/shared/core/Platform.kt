package dev.teogor.verbatim.shared.core

/**
 * Platform information for the current device.
 */
interface Platform {
    /** Human-readable platform name (e.g., "Android 14", "iOS 17.2", "Java 21"). */
    val name: String
}

/**
 * Returns the [Platform] for the current target.
 */
expect fun getPlatform(): Platform
