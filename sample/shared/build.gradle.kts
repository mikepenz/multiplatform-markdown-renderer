plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
}

kotlin {
    androidLibrary {
        namespace = "com.mikepenz.markdown.sample.shared"
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
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

    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatformMarkdownRenderer)
            api(projects.multiplatformMarkdownRendererM2)
            api(projects.multiplatformMarkdownRendererM3)
            api(projects.multiplatformMarkdownRendererCoil3)
            api(projects.multiplatformMarkdownRendererCode)

            implementation(baseLibs.jetbrains.compose.foundation)
            implementation(baseLibs.jetbrains.compose.ui)
            implementation(baseLibs.jetbrains.compose.ui.util)
            implementation(baseLibs.jetbrains.compose.components.resources)
            implementation(baseLibs.jetbrains.compose.material)
            implementation(baseLibs.jetbrains.compose.material3)

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

compose.resources {
    packageOfResClass = "com.mikepenz.markdown.sample.shared.resources"
}

aboutLibraries {
    export {
        outputPath = file("src/commonMain/composeResources/files/aboutlibraries.json")
    }
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
}
