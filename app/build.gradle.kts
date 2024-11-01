import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("android")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.screenshot)
}

if (openSourcSigningFile != null) {
    apply(from = openSourcSigningFile)
}

android {
    namespace = "com.mikepenz.markdown.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        minSdk = 24 // anything below faces: https://github.com/mikepenz/multiplatform-markdown-renderer/issues/223
        targetSdk = libs.versions.targetSdk.get().toInt()

        versionCode = property("VERSION_CODE").toString().toInt()
        versionName = property("VERSION_NAME").toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "markdown-renderer-sample-v$versionName-c$versionCode")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.findByName("debug")
        }

        getByName("release") {
            signingConfig = signingConfigs.findByName("release")

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

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

private val openSourcSigningFile: String?
    get() {
        val k = "openSource.signing.file"
        return Properties().also { prop ->
            rootProject.file("local.properties").takeIf { it.exists() }?.let {
                prop.load(it.inputStream())
            }
        }.getProperty(k, null) ?: if (project.hasProperty(k)) project.property(k)
            ?.toString() else null
    }
