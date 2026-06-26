plugins {
    id("verbatim.kmp.ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.verbatimCore)
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

winds {
    moduleMetadata {
        name = "UI"
    }
}
