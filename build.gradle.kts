import dev.teogor.winds.api.ArtifactIdFormat
import dev.teogor.winds.api.License
import dev.teogor.winds.api.NameFormat
import dev.teogor.winds.api.Person
import dev.teogor.winds.api.Scm
import dev.teogor.winds.api.TicketSystem
import dev.teogor.winds.ktx.createVersion
import dev.teogor.winds.ktx.person
import dev.teogor.winds.ktx.scm
import dev.teogor.winds.ktx.ticketSystem

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.build.config) apply false
    alias(libs.plugins.gradle.publish) apply false

    alias(libs.plugins.teogor.winds)
    alias(libs.plugins.vanniktech.maven)
}

val isLocalPublish = gradle.startParameter.taskNames.any { it.contains("Local", ignoreCase = true) }

winds {
    features {
        mavenPublishing = true
        docsGenerator = true
        workflowSynthesizer = true
    }

    moduleMetadata {
        name = "Verbatim"
        description = """
        |📖 Verbatim is a zero-allocation structured logging and telemetry library for Kotlin Multiplatform (KMP) supporting automatic PII masking, performance tracing, and crash sinks.
        |""".trimMargin()

        yearCreated = 2026
        websiteUrl = "https://source.teogor.dev/verbatim/"
        apiDocsUrl = "https://source.teogor.dev/verbatim/html/"

        artifactDescriptor {
            group = "dev.teogor.verbatim"
            name = "verbatim"
            version = createVersion(1, 0, 0) {
                alphaRelease(1)
            }
            nameFormat = NameFormat.FULL
            artifactIdFormat = ArtifactIdFormat.MODULE_NAME_ONLY
        }

        // Providing SCM (Source Control Management)
        scm<Scm.GitHub> {
            owner = "teogor"
            repository = "verbatim"
        }

        // Providing Ticket System
        ticketSystem<TicketSystem.GitHub> {
            owner = "teogor"
            repository = "verbatim"
        }

        // Providing Licenses
        licensedUnder(License.Apache2())

        // Providing Persons
        person<Person.DeveloperContributor> {
            id = "teogor"
            name = "Teodor Grigor"
            email = "open-source@teogor.dev"
            url = "https://teogor.dev"
            roles = listOf("Code Owner", "Developer", "Designer", "Maintainer")
            timezone = "UTC+2"
            organization = "Teogor"
            organizationUrl = "https://github.com/teogor"
        }
    }

    publishing {
        enabled = false
        enablePublicationSigning = !isLocalPublish
        optInForVanniktechPlugin = true
        cascade = true
        automaticPublishing = false
    }

    documentationBuilder {
        htmlPath = "html/"
    }
}
