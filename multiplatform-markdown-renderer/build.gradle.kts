import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

base {
    archivesBaseName = "multiplatform-markdown-renderer"
}

android {
    compileSdk = Versions.androidCompileSdk

    defaultConfig {
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    jvm()

    // js {
    //     nodejs {}
    //     browser {}
    //     compilations.all {
    //         kotlinOptions {
    //             moduleKind = "umd"
    //             sourceMap = true
    //             sourceMapEmbedSources = null
    //         }
    //     }
    // }

    android {
        publishAllLibraryVariants()
    }
}

dependencies {
    commonMainApi(Deps.Markdown.core)
    commonMainCompileOnly(compose.runtime)
    commonMainCompileOnly(compose.ui)
    commonMainCompileOnly(compose.foundation)
    commonMainCompileOnly(compose.material)

    add("androidMainImplementation", Deps.Compose.coilCompose)
}

tasks.dokkaHtml.configure {
    dokkaSourceSets {
        configureEach {
            noAndroidSdkLink.set(false)
        }
    }
}

tasks.create<Jar>("javadocJar") {
    dependsOn("dokkaJavadoc")
    classifier = "javadoc"
    from("$buildDir/javadoc")
}

mavenPublish {
    releaseSigningEnabled = true
    androidVariantToPublish = "release"
}

publishing {
    repositories {
        maven {
            name = "installLocally"
            setUrl("${rootProject.buildDir}/localMaven")
        }
    }
}