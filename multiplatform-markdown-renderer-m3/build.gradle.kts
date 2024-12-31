import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.mavenPublish)
}

android {
    namespace = "com.mikepenz.markdown.m3"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

kotlin {
    applyDefaultHierarchyTemplate()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                languageVersion.set(KotlinVersion.KOTLIN_1_9)
                apiVersion.set(KotlinVersion.KOTLIN_1_9)
            }
        }
    }

    androidTarget {
        publishLibraryVariants("release")
    }
    jvm {
        compilations {
            all {
                kotlinOptions.jvmTarget = "11"
            }
        }
    }
    js(IR) {
        nodejs()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val fileBasedTest by creating {
            dependsOn(commonTest)
        }
        val jvmTest by getting {
            dependsOn(fileBasedTest)
        }
        val jsTest by getting {
            dependsOn(fileBasedTest)
        }
        val nativeMain by getting {
            dependsOn(commonMain)
        }
        val nativeTest by getting {
            dependsOn(fileBasedTest)
        }
        val nativeSourceSets = listOf(
            "macosX64",
            "macosArm64",
            "ios",
            "iosSimulatorArm64"
        ).map { "${it}Main" }
        for (set in nativeSourceSets) {
            getByName(set).dependsOn(nativeMain)
        }
        val nativeTestSourceSets = listOf(
            "macosX64",
            "macosArm64"
        ).map { "${it}Test" }
        for (set in nativeTestSourceSets) {
            getByName(set).dependsOn(nativeTest)
            getByName(set).dependsOn(fileBasedTest)
        }
    }
}

dependencies {
    commonMainApi(projects.multiplatformMarkdownRenderer)
    commonMainApi(libs.markdown)

    commonMainCompileOnly(compose.runtime)
    commonMainCompileOnly(compose.material3)
}

dokka {
    dokkaSourceSets {
        configureEach {
            enableAndroidDocumentationLink.set(true)
        }
    }
}

tasks.create<Jar>("javadocJar") {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    from("${layout.buildDirectory}/javadoc")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()
}

publishing {
    repositories {
        maven {
            name = "installLocally"
            setUrl("${rootProject.layout.buildDirectory}/localMaven")
        }
    }
}