plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

android {
    namespace = "com.mikepenz.markdown.code"
}

dependencies {
    commonMainApi(projects.multiplatformMarkdownRenderer)
    commonMainCompileOnly(compose.runtime)
    commonMainCompileOnly(compose.ui)
    commonMainCompileOnly(compose.foundation)

    commonMainApi(libs.highlights)
}