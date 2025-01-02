plugins {
    id("com.mikepenz.convention-plugin.android-library")
    id("com.mikepenz.convention-plugin.kotlin-multiplatform")
    id("com.mikepenz.convention-plugin.compose")
    id("com.mikepenz.convention-plugin.publishing")
}

android {
    namespace = "com.mikepenz.markdown.coil3"
}

dependencies {
    commonMainApi(projects.multiplatformMarkdownRenderer)
    commonMainApi(libs.coil.core)
    commonMainCompileOnly(compose.runtime)
}