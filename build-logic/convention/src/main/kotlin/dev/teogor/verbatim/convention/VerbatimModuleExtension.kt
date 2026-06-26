package dev.teogor.verbatim.convention

import org.gradle.api.Project

/**
 * Extension for configuring Verbatim KMP module behavior.
 */
open class VerbatimModuleExtension {
    /** The target preset for this module. */
    var targetPreset: TargetPreset = TargetPreset.LIBRARY
}

fun Project.verbatimModule(block: VerbatimModuleExtension.() -> Unit = {}): VerbatimModuleExtension {
    val extension = extensions.findByType(VerbatimModuleExtension::class.java)
        ?: extensions.create("verbatimModule", VerbatimModuleExtension::class.java)
    extension.block()
    return extension
}
