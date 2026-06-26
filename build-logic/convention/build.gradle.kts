import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "dev.teogor.verbatim.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

// Resolve the version catalog outside of DependencyHandler scope
val buildLibs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    compileOnly("com.android.tools.build:gradle:${buildLibs.findVersion("agp").get().requiredVersion}")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${buildLibs.findVersion("kotlin").get().requiredVersion}")
    compileOnly("org.jetbrains.compose:compose-gradle-plugin:${buildLibs.findVersion("composeMultiplatform").get().requiredVersion}")
    compileOnly("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${buildLibs.findVersion("kotlin").get().requiredVersion}")
    compileOnly("dev.teogor.winds:dev.teogor.winds.gradle.plugin:${buildLibs.findVersion("teogor-winds").get().requiredVersion}")
    compileOnly("com.vanniktech:gradle-maven-publish-plugin:${buildLibs.findVersion("vanniktech-maven").get().requiredVersion}")
    compileOnly("com.github.gmazzo.buildconfig:plugin:${buildLibs.findVersion("build-config").get().requiredVersion}")
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "verbatim.kmp.library"
            implementationClass = "dev.teogor.verbatim.convention.KmpLibraryConventionPlugin"
        }
        register("kmpUi") {
            id = "verbatim.kmp.ui"
            implementationClass = "dev.teogor.verbatim.convention.KmpUiConventionPlugin"
        }
        register("kmpApp") {
            id = "verbatim.kmp.app"
            implementationClass = "dev.teogor.verbatim.convention.KmpAppConventionPlugin"
        }
        register("kmpAppUi") {
            id = "verbatim.kmp.app.ui"
            implementationClass = "dev.teogor.verbatim.convention.KmpAppUiConventionPlugin"
        }
        register("gradlePlugin") {
            id = "verbatim.gradle.plugin"
            implementationClass = "dev.teogor.verbatim.convention.GradlePluginConventionPlugin"
        }
    }
}
