plugins {
    id("com.mikepenz.android.library")
    id("com.mikepenz.kotlin.multiplatform")
    id("com.mikepenz.compose")
    id("com.mikepenz.publishing")
}

android {
    namespace = "com.mikepenz.markdown.coil2"
}

dependencies {
    commonMainApi(projects.multiplatformMarkdownRenderer)
    commonMainCompileOnly(compose.runtime)
    commonMainCompileOnly(compose.ui)

    "androidMainApi"(libs.coil2.core)
}