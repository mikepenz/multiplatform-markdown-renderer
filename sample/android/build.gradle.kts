plugins {
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.mikepenz.aboutlibraries.plugin.android")
}

android {
    namespace = "com.mikepenz.markdown.sample"

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        base.archivesName = "markdown-renderer-sample-v$versionName-c$versionCode"
    }
}

dependencies {
    implementation(project(":sample:shared"))
    implementation(baseLibs.jetbrains.compose.foundation)
    implementation(baseLibs.jetbrains.compose.material)
    implementation(baseLibs.jetbrains.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ktor.client.okhttp)
    debugImplementation(baseLibs.jetbrains.compose.ui.tooling)
}

aboutLibraries {
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
    export {
        exportVariant = "release"
    }
}
