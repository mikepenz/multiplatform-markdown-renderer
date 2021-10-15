import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
}

if (openSourcSigningFile != null) {
    apply(from = openSourcSigningFile)
}

android {
    compileSdk = Versions.androidCompileSdk

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk

        versionCode = 100
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
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
