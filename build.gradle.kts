plugins {
    alias(baseLibs.plugins.conventionPlugin)

    // this is necessary to avoid the plugins to be loaded multiple times in each subproject's classloader
    alias(baseLibs.plugins.androidApplication) apply false
    alias(baseLibs.plugins.androidLibrary) apply false
    alias(baseLibs.plugins.composeMultiplatform) apply false
    alias(baseLibs.plugins.composeCompiler) apply false
    alias(baseLibs.plugins.kotlinMultiplatform) apply false
    alias(baseLibs.plugins.dokka)
    alias(baseLibs.plugins.aboutLibraries) apply false
    alias(baseLibs.plugins.mavenPublish) apply false
    alias(baseLibs.plugins.binaryCompatiblityValidator) apply false
    alias(baseLibs.plugins.versionCatalogUpdate) apply false
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
    }
}