rootProject.name = "multiplatform-markdown-renderer-root"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenLocal()
    }

    versionCatalogs {
        create("baseLibs") {
            from("com.mikepenz:version-catalog:0.2.2")
        }
    }
}

include(":multiplatform-markdown-renderer")
include(":multiplatform-markdown-renderer-m2")
include(":multiplatform-markdown-renderer-m3")
include(":multiplatform-markdown-renderer-coil2")
include(":multiplatform-markdown-renderer-coil3")
include(":multiplatform-markdown-renderer-code")

include(":app")
include(":app-desktop")
include(":app-wasm")