@file:Suppress("UnstableApiUsage")

rootProject.name = "Verbatim"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":app:shared-core")
include(":app:shared-ui")
include(":app:androidApp")
include(":app:desktopApp")
include(":app:serverApp")
include(":app:terminalApp")
include(":app:webApp")

// include(":verbatim-compiler-plugin")
include(":verbatim-core")
include(":verbatim-crashlytics")
// include(":verbatim-ktor")
include(":verbatim-middleware")
// include(":verbatim-persistence")
// include(":verbatim-tracing")
// include(":verbatim-ui")
