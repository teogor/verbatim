package dev.teogor.verbatim.shared.core

import kotlin.test.Test
import kotlin.test.assertNotNull

class SharedCoreTest {

    @Test
    fun platformIsAvailable() {
        val platform = getPlatform()
        assertNotNull(platform.name)
    }
}
