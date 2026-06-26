
plugins {
    id("verbatim.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.verbatimCore)
            implementation(libs.okio.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

winds {
    moduleMetadata {
        name = "Persistence"
    }
}
