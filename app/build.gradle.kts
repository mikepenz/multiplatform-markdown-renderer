plugins {
    id("com.android.application")
    kotlin("android")
}

if (property("openSource.signing.file") != null) {
    apply(from = property("openSource.signing.file"))
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
        implementation(lifecycleRuntimeKtx)
        implementation(lifecycleViewmodelKtx)
        implementation(activityCompose)
    }

    with(Deps.Compose) {
        implementation(ui)
        implementation(uiGraphics)
        implementation(foundationLayout)
        implementation(material)
        implementation(coilCompose)
        implementation(accompanistInsets)
        implementation(accompanistInsetsUi)
        implementation(uiTooling)
    }
}