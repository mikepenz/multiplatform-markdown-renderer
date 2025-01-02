import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("android")
    id("com.mikepenz.android.application")
    id("com.mikepenz.kotlin.multiplatform")
    id("com.mikepenz.compose")
    id("com.mikepenz.aboutlibraries.plugin")
    alias(libs.plugins.screenshot)
}

if (openSourceSigningFile != null) {
    apply(from = openSourceSigningFile)
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

private val openSourceSigningFile: String?
    get() {
        val k = "openSource.signing.file"
        return Properties().also { prop ->
            rootProject.file("local.properties").takeIf { it.exists() }?.let {
                prop.load(it.inputStream())
            }
        }.getProperty(k, null) ?: if (project.hasProperty(k)) project.property(k)
            ?.toString() else null
    }
