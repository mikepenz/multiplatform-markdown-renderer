plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.code"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatformMarkdownRenderer)
            compileOnly(baseLibs.jetbrains.compose.runtime)
            compileOnly(baseLibs.jetbrains.compose.ui)
            compileOnly(baseLibs.jetbrains.compose.foundation)

            api(libs.highlights)
        }
    }
}
