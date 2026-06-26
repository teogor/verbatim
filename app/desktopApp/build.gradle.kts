import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.app.sharedUi)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.swing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "dev.teogor.verbatim.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.teogor.verbatim"
            packageVersion = "1.0.0"
        }
    }
}