plugins {
    id("verbatim.kmp.app")
}

kotlin {
    androidLibrary {
        namespace = "dev.teogor.verbatim.app.shared.core"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.verbatimCore)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}
