rootProject.name = "multiplatform-markdown-renderer-root"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

include(":multiplatform-markdown-renderer")
include(":multiplatform-markdown-renderer-m2")
include(":multiplatform-markdown-renderer-m3")

include(":app")
include(":compose-desktop")
include(":app-desktop")