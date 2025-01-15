plugins {
    id("com.mikepenz.convention.kotlin")
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
    alias(baseLibs.plugins.screenshot)
}

android {
    namespace = "com.mikepenz.markdown.app"

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        setProperty("archivesBaseName", "markdown-renderer-sample-v$versionName-c$versionCode")
    }

    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(projects.multiplatformMarkdownRenderer)
    implementation(projects.multiplatformMarkdownRendererM2)
    implementation(projects.multiplatformMarkdownRendererCoil3)

    implementation(libs.androidx.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.coil)
    implementation(libs.coil.svg)

    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.uiTooling)
}

aboutLibraries {
    registerAndroidTasks = false
    duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
}