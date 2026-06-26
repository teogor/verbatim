package dev.teogor.verbatim.shared.core

class MinGWPlatform : Platform {
    override val name: String = "Windows (MinGW)"
}

actual fun getPlatform(): Platform = MinGWPlatform()
