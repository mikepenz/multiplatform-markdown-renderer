rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

pluginManagement {
    plugins {
        id("com.vanniktech.maven.publish") version "0.30.0"
    }
}

include(":convention")
