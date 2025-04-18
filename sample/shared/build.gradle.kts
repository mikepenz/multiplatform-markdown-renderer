plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
}

kotlin {
    androidTarget()

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

    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatformMarkdownRenderer)
            api(projects.multiplatformMarkdownRendererM2)
            api(projects.multiplatformMarkdownRendererM3)
            api(projects.multiplatformMarkdownRendererCoil3)
            api(projects.multiplatformMarkdownRendererCode)

            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.uiUtil)
            implementation(compose.components.resources)
            implementation(compose.material)
            implementation(compose.material3)

            // required for coil
            implementation(libs.ktor.client.core)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.svg)

            // about libs
            api(baseLibs.bundles.aboutlibs)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.mikepenz.markdown.sample.shared"
}

compose.resources {
    packageOfResClass = "com.mikepenz.markdown.sample.shared.resources"
}

aboutLibraries {
    android {
        registerAndroidTasks = false
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
}
