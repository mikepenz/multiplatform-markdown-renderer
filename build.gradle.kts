buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.0")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
        classpath("com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:10.1.0")
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.1.1")
    }
}

allprojects {
    group = ext.get("GROUP")!!
    version = ext.get("VERSION_NAME")!!

    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }
    }
}

subprojects {
    apply(from = "../detekt.gradle")

    dependencies {
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
    }
}