pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "multiplatform-markdown-renderer"

include(":multiplatform-markdown-renderer")
include(":app")
include(":compose-desktop")