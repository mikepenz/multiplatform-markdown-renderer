import org.gradle.kotlin.dsl.sourceSets
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "markdown"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.multiplatformMarkdownRenderer)
            implementation(projects.multiplatformMarkdownRendererM2)
            implementation(projects.multiplatformMarkdownRendererM3)
            implementation(projects.multiplatformMarkdownRendererCoil3)
            implementation(projects.multiplatformMarkdownRendererCode)

            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.uiUtil)
            implementation(compose.components.resources)

            // required for coil
            implementation(libs.ktor.client.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.svg)

            // about libs
            implementation(baseLibs.bundles.aboutlibs)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.java)
        }
    }
}

android {
    namespace = "com.mikepenz.markdown.sample"

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        setProperty("archivesBaseName", "markdown-renderer-sample-v$versionName-c$versionCode")
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.resources {
    packageOfResClass = "com.mikepenz.markdown.sample.resources"
}

compose.desktop {
    application {
        mainClass = "com.mikepenz.markdown.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.mikepenz.markdown"
            packageVersion = "1.0.0"
        }
    }
}

aboutLibraries {
    registerAndroidTasks = false
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
}
