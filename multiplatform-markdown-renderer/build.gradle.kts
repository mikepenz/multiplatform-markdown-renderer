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
        commonMain.dependencies {
            api(libs.markdown)
            api(baseLibs.kotlinx.collections.immutable)

            compileOnly(baseLibs.jetbrains.compose.runtime)
            compileOnly(baseLibs.jetbrains.compose.ui)
            compileOnly(baseLibs.jetbrains.compose.foundation)
        }
    }
}

composeCompiler {
    stabilityConfigurationFiles.add(project.layout.projectDirectory.file("stability_config.conf"))
}
