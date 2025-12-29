plugins {
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.mikepenz.aboutlibraries.plugin.android")
    alias(baseLibs.plugins.screenshot)
}

android {
    namespace = "com.mikepenz.markdown.sample"

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        base.archivesName = "markdown-renderer-sample-v$versionName-c$versionCode"
    }

    @Suppress("UnstableApiUsage")
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(project(":sample:shared"))
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ktor.client.okhttp)
    debugImplementation(compose.uiTooling)
    "screenshotTestImplementation"(compose.uiTooling)
}

aboutLibraries {
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
    export {
        exportVariant = "release"
    }
}
