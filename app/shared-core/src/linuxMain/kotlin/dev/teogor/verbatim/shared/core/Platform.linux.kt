package dev.teogor.verbatim.shared.core

class LinuxPlatform : Platform {
    override val name: String = "Linux"
}

actual fun getPlatform(): Platform = LinuxPlatform()
