plugins {
    `kotlin-dsl`
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
        register("kotlinMultiplatform") {
            id = "com.mikepenz.kotlin.multiplatform"
            implementationClass = "com.mikepenz.gradle.KotlinMultiplatformConventionPlugin"
        }

        register("root") {
            id = "com.mikepenz.root"
            implementationClass = "com.mikepenz.gradle.RootConventionPlugin"
        }

        register("androidApplication") {
            id = "com.mikepenz.android.application"
            implementationClass = "com.mikepenz.gradle.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.mikepenz.android.library"
            implementationClass = "com.mikepenz.gradle.AndroidLibraryConventionPlugin"
        }

        register("androidTest") {
            id = "com.mikepenz.android.test"
            implementationClass = "com.mikepenz.gradle.AndroidTestConventionPlugin"
        }

        register("compose") {
            id = "com.mikepenz.compose"
            implementationClass = "com.mikepenz.gradle.ComposeConventionPlugin"
        }

        register("publishing") {
            id = "com.mikepenz.publishing"
            implementationClass = "com.mikepenz.gradle.PublishingConventionPlugin"
        }
    }
}
