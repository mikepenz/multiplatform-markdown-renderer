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

    commonMainApi(libs.coil.core) {
        exclude(group = "org.jetbrains.compose.runtime")
    }
    commonMainCompileOnly(compose.runtime)
}
