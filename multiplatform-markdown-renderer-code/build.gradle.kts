plugins {
    id("com.mikepenz.convention-plugin.android-library")
    id("com.mikepenz.convention-plugin.kotlin-multiplatform")
    id("com.mikepenz.convention-plugin.compose")
    id("com.mikepenz.convention-plugin.publishing")
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