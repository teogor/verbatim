plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    applyDefaultHierarchyTemplate()

    // Native executables for desktop platforms
    listOf(
        macosArm64(),
        linuxX64(),
        mingwX64(),
    ).forEach { target ->
        target.binaries.executable {
            entryPoint = "dev.teogor.verbatim.terminal.main"
        }
    }

    // JVM target for development
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.sharedCore)
            implementation(projects.verbatimCore)
        }
    }
}
