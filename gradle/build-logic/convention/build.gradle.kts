import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar

plugins {
    `kotlin-dsl`
    id("com.vanniktech.maven.publish")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.mavenPublish.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("root") {
            id = "com.mikepenz.convention-plugin.root"
            implementationClass = "com.mikepenz.gradle.RootConventionPlugin"
        }

        register("kotlinMultiplatform") {
            id = "com.mikepenz.convention-plugin.kotlin-multiplatform"
            implementationClass = "com.mikepenz.gradle.KotlinMultiplatformConventionPlugin"
        }

        register("androidApplication") {
            id = "com.mikepenz.convention-plugin.android-application"
            implementationClass = "com.mikepenz.gradle.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.mikepenz.convention-plugin.android-library"
            implementationClass = "com.mikepenz.gradle.AndroidLibraryConventionPlugin"
        }

        register("androidTest") {
            id = "com.mikepenz.convention-plugin.android-test"
            implementationClass = "com.mikepenz.gradle.AndroidTestConventionPlugin"
        }

        register("compose") {
            id = "com.mikepenz.convention-plugin.compose"
            implementationClass = "com.mikepenz.gradle.ComposeConventionPlugin"
        }

        register("publishing") {
            id = "com.mikepenz.convention-plugin.publishing"
            implementationClass = "com.mikepenz.gradle.PublishingConventionPlugin"
        }
    }
}

mavenPublishing {
    configure(GradlePlugin(
        // configures the -javadoc artifact, possible values:
        // - `JavadocJar.None()` don't publish this artifact
        // - `JavadocJar.Empty()` publish an emprt jar
        // - `JavadocJar.Javadoc()` to publish standard javadocs
        // - `JavadocJar.Dokka("dokkaHtml")` when using Kotlin with Dokka, where `dokkaHtml` is the name of the Dokka task that should be used as input
        javadocJar = JavadocJar.Javadoc(),
        sourcesJar = true,
    ))
}