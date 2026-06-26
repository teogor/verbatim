package dev.teogor.verbatim.shared.core

import platform.Foundation.NSProcessInfo

class ApplePlatform : Platform {
    override val name: String = NSProcessInfo.processInfo.processName
}

actual fun getPlatform(): Platform = ApplePlatform()
