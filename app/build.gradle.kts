import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
}

if (openSourcSigningFile != null) {
    apply(from = openSourcSigningFile)
}

android {
    namespace = "com.mikepenz.markdown.app"
    compileSdk = Versions.androidCompileSdk

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk

        versionCode = property("VERSION_CODE").toString().toInt()
        versionName = property("VERSION_NAME").toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "markdown-renderer-sample-v$versionName-c$versionCode")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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

}

dependencies {
    implementation(project(":multiplatform-markdown-renderer"))

    with(Deps.Android) {
        implementation(material)
    }

    with(Deps.AndroidX) {
        implementation(activityCompose)
    }


    with(Deps.Compose) {
        implementation(coilCompose)

        implementation(foundationLayout)
        implementation(material)
        implementation(ui)
        implementation(uiTooling)
        implementation(uiGraphics)
    }
}

private val openSourcSigningFile: String?
    get() {
        val k = "openSource.signing.file"
        return Properties().also { prop ->
            rootProject.file("local.properties").takeIf { it.exists() }?.let {
                prop.load(it.inputStream())
            }
        }.getProperty(k, null) ?: if (project.hasProperty(k)) project.property(k)?.toString() else null
    }
