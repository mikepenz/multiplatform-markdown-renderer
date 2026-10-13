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
            api(baseLibs.kotlinx.coroutines.core)
            api(baseLibs.kotlinx.collections.immutable)

            compileOnly(baseLibs.jetbrains.compose.runtime)
            compileOnly(baseLibs.jetbrains.compose.ui)
            compileOnly(baseLibs.jetbrains.compose.foundation)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(baseLibs.kotlinx.coroutines.test)
        }
        jvmTest.dependencies {
            implementation(baseLibs.jetbrains.compose.runtime)
            implementation(baseLibs.jetbrains.compose.ui)
            implementation(baseLibs.jetbrains.compose.foundation)
            implementation(baseLibs.jetbrains.compose.ui.test)
            implementation(compose.desktop.currentOs)
        }
    }
}

composeCompiler {
    stabilityConfigurationFiles.add(project.layout.projectDirectory.file("stability_config.conf"))
}
