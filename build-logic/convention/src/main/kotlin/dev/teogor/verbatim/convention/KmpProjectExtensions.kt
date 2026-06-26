package dev.teogor.verbatim.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Target presets for KMP modules.
 *
 * @property LIBRARY Standard 5 targets: Android, iOS (arm64 + simArm64), JVM, JS, WasmJS.
 * @property FULL All LIBRARY targets + desktop native (macOS, Linux, MinGW)
 *   and Apple embedded (tvOS, watchOS).
 */
enum class TargetPreset {
    LIBRARY,
    FULL
}

@OptIn(ExperimentalWasmDsl::class)
fun KotlinMultiplatformExtension.configureKmpTargets(
    project: Project,
    preset: TargetPreset = TargetPreset.LIBRARY,
    frameworkBaseName: String? = null
) {
    androidTarget(project)

    configureNonAndroidTargets(project, preset, frameworkBaseName)
}

@OptIn(ExperimentalWasmDsl::class)
fun KotlinMultiplatformExtension.configureNonAndroidTargets(
    project: Project,
    preset: TargetPreset = TargetPreset.LIBRARY,
    frameworkBaseName: String? = null
) {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = frameworkBaseName ?: project.name
            isStatic = true
        }
    }

    jvm()
    js { browser() }
    wasmJs { browser() }

    if (preset == TargetPreset.FULL) {
        // Desktop native
        macosArm64()
        linuxX64()
        mingwX64()

        // Apple embedded
        tvosArm64()
        tvosSimulatorArm64()
        watchosArm64()
        watchosSimulatorArm64()
    }

    // Suppress expect/actual classes Beta warning (extension-level)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    // Disable test tasks for simulator targets unsupported by Xcode
    disableUnsupportedSimulatorTestTasks(project)
}

fun KotlinMultiplatformExtension.androidTarget(
    project: Project,
    namespace: String = project.defaultNamespace,
    configure: Action<KotlinMultiplatformAndroidLibraryTarget> = Action {},
) {
    val libs = project.versionCatalog
    (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>(
        "android",
    ) {
        this.namespace = namespace
        compileSdk = libs.requireVersionInt("android-compileSdk")
        minSdk = libs.requireVersionInt("android-minSdk")

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }

        configure.execute(this)
    }
}

val Project.defaultNamespace: String
    get() {
        val basePackage = "dev.teogor.verbatim"
        val suffix = path.replace(":", ".")
            .removePrefix(".")
            .replace("verbatim-", "")
        return if (suffix.isEmpty()) basePackage else "$basePackage.$suffix"
    }

/**
 * Disables test tasks for Apple embedded simulator targets that Xcode
 * does not support. For example, Xcode cannot run simulator tests for
 * tvOS arm64, so these tasks are disabled to prevent build failures.
 */
private fun disableUnsupportedSimulatorTestTasks(project: Project) {
    val unsupportedTargets = setOf(
        "tvosSimulatorArm64",
    )

    project.tasks.configureEach {
        val isTestTask = name.contains("Test", ignoreCase = true)
        val isUnsupportedTarget = unsupportedTargets.any { target ->
            name.contains(target, ignoreCase = true)
        }
        if (isTestTask && isUnsupportedTarget) {
            enabled = false
        }
    }
}
