plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.markdown)
                api(baseLibs.kotlinx.collections.immutable)

                compileOnly(compose.runtime)
                compileOnly(compose.ui)
                compileOnly(compose.foundation)
            }
        }
    }
}

composeCompiler {
    stabilityConfigurationFiles.add(project.layout.projectDirectory.file("stability_config.conf"))
}