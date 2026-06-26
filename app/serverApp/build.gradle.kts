plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

application {
    mainClass.set("dev.teogor.verbatim.server.MainKt")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.app.sharedCore)
    implementation(projects.verbatimCore)
    // implementation(projects.verbatimPersistence)
    implementation(libs.kotlinx.coroutines.core)
}
