plugins {
    id("com.mikepenz.android.library")
    id("com.mikepenz.kotlin.multiplatform")
    id("com.mikepenz.compose")
    id("com.mikepenz.publishing")
}

android {
    namespace = "com.mikepenz.markdown.m3"
}

dependencies {
    commonMainApi(projects.multiplatformMarkdownRenderer)
    commonMainApi(libs.markdown)

    commonMainCompileOnly(compose.runtime)
    commonMainCompileOnly(compose.material3)
}