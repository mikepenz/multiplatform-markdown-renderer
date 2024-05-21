plugins {
    // this is necessary to avoid the plugins to be loaded multiple times in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.aboutlibraries) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.mavenPublish) apply false
}

allprojects {
    group = ext.get("GROUP")!!
    version = ext.get("VERSION_NAME")!!

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven { setUrl("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental") }
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

// subprojects {
//     apply(from = "../detekt.gradle")
//     dependencies {
//         "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
//     }
// }