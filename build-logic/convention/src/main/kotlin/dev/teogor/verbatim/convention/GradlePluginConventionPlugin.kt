package dev.teogor.verbatim.convention

import com.github.gmazzo.buildconfig.BuildConfigExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class GradlePluginConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-gradle-plugin")
                apply("org.jetbrains.kotlin.jvm")
                apply("dev.teogor.winds")
                apply("com.vanniktech.maven.publish")
                apply("com.gradle.plugin-publish")
                apply("com.github.gmazzo.buildconfig")
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11

                sourceSets.getByName("main") {
                    java.srcDir("src/jvmMain/kotlin")
                }
                sourceSets.getByName("test") {
                    java.srcDir("src/jvmTest/kotlin")
                }
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
                }
            }

            extensions.configure<BuildConfigExtension> {
                packageName("dev.teogor.verbatim")

                afterEvaluate {
                    buildConfigField("String", "NAME", "\"${group}\"")
                    buildConfigField("String", "VERSION", "\"${version}\"")
                }
            }
        }
    }
}
