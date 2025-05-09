plugins {
    id("com.mikepenz.convention.android-library")
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

android {
    namespace = "com.mikepenz.markdown.coil3"
}

dependencies {
    commonMainApi(projects.multiplatformMarkdownRenderer)

    // TODO reverse once coil3 was released based on compose 1.8.0
    commonMainApi(libs.coil.core) {
        exclude(group = "org.jetbrains.compose.runtime")
    }
    commonMainCompileOnly(compose.runtime)
    commonMainCompileOnly(compose.runtimeSaveable)
    commonMainCompileOnly("org.jetbrains.compose.ui:ui-backhandler:1.8.0-rc01")
}