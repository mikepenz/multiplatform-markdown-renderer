plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.coil2"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatformMarkdownRenderer)
            compileOnly(baseLibs.jetbrains.compose.runtime)
            compileOnly(baseLibs.jetbrains.compose.ui)
        }

        androidMain.dependencies {
            api(libs.coil2.core)
        }
    }
}
