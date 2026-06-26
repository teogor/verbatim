
plugins {
    id("verbatim.kmp.library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.verbatimCore)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

winds {
    moduleMetadata {
        name = "Middleware"
    }
}
