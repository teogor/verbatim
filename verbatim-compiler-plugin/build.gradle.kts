plugins {
    id("verbatim.gradle.plugin")
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlin.test)
}

gradlePlugin {
    website.set("https://source.teogor.dev/verbatim")
    vcsUrl.set("https://github.com/teogor/verbatim")

    plugins {
        register("verbatimCompiler") {
            id = "dev.teogor.verbatim.compiler"
            implementationClass = "dev.teogor.verbatim.compiler.VerbatimCompilerPlugin"
            displayName = "Verbatim Compiler Plugin"
            description =
                "The Verbatim Compiler Plugin permanently purges targeted logging bytecode allocations from production binaries during compilation."
        }
    }
}

winds {
    moduleMetadata {
        name = "Compiler Plugin"
    }

    documentationBuilder {
        isCompiler = true
    }
}
