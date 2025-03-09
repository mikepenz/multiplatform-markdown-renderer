plugins {
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.kotlin")
    id("com.mikepenz.convention.compose")
    alias(baseLibs.plugins.screenshot)
}

android {
    namespace = "com.mikepenz.markdown.app"

    defaultConfig {
        applicationId = "com.mikepenz.markdown.screenshottest"
        setProperty("archivesBaseName", "markdown-renderer-sample-v$versionName-c$versionCode")
    }

    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(projects.multiplatformMarkdownRenderer)
    implementation(projects.multiplatformMarkdownRendererM2)
    implementation(projects.multiplatformMarkdownRendererM3)
    implementation(projects.multiplatformMarkdownRendererCoil3)

    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.uiTooling)

    "screenshotTestImplementation"(compose.uiTooling)
}
