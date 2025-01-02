import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
}

group = "com.mikepenz"
version = "1.0.0"

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(projects.multiplatformMarkdownRenderer)
                implementation(projects.multiplatformMarkdownRendererM2)
                implementation(projects.multiplatformMarkdownRendererCoil3)
                implementation(projects.multiplatformMarkdownRendererCode)

                implementation(compose.components.resources)
                implementation(compose.desktop.currentOs)
                implementation(compose.foundation)
                implementation(compose.material)

                // about libs
                implementation(baseLibs.bundles.aboutlibs)

                // required for coil
                implementation(libs.coil.network.ktor)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.java)
                implementation(baseLibs.kotlinx.coroutines.swing)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "multiplatform-markdown-renderer"
            packageVersion = "1.0.0"
        }
    }
}

aboutLibraries {
    registerAndroidTasks = false
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
}
