plugins {
    id("verbatim.kmp.app.ui")
}

compose.resources {
    packageOfResClass = "dev.teogor.verbatim.shared.ui.resources"
}

kotlin {
    androidLibrary {
        namespace = "dev.teogor.verbatim.app.shared.ui"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.app.sharedCore)
            // api(projects.verbatimUi)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
