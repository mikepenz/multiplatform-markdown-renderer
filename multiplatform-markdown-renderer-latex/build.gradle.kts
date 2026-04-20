plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
    alias(baseLibs.plugins.kotlinSerialization)
}

// Download RaTeX XCFramework from GitHub Releases
val ratexVersion = libs.versions.ratex.get()
val xcframeworkZip = layout.buildDirectory.file("ratex/RaTeX.xcframework.zip")
val xcframeworkDir = layout.buildDirectory.dir("ratex/RaTeX.xcframework")

val downloadRaTeXXCFramework by tasks.registering {
    val zipFile = xcframeworkZip.get().asFile
    val outDir = xcframeworkDir.get().asFile
    inputs.property("ratexVersion", ratexVersion)
    outputs.dir(outDir)
    doLast {
        if (!outDir.resolve("ios-arm64/libratex_ffi.a").exists()) {
            zipFile.parentFile.mkdirs()
            val url = "https://github.com/erweixin/RaTeX/releases/download/v$ratexVersion/RaTeX.xcframework.zip"
            logger.lifecycle("Downloading RaTeX XCFramework from $url")
            uri(url).toURL().openStream().use { input ->
                zipFile.outputStream().use { output -> input.copyTo(output) }
            }
            copy {
                from(zipTree(zipFile))
                into(outDir.parentFile)
            }
            zipFile.delete()
        }
    }
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.latex"
        androidResources.enable = true
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.compilations.getByName("main") {
            cinterops {
                create("ratex") {
                    defFile(project.file("src/nativeInterop/cinterop/ratex.def"))

                    val xcfDir = xcframeworkDir.get().asFile
                    val sliceDir = when (target.konanTarget.name) {
                        "ios_arm64" -> "ios-arm64"
                        "ios_simulator_arm64", "ios_x64" -> "ios-arm64_x86_64-simulator"
                        else -> null
                    }
                    if (sliceDir != null) {
                        includeDirs(xcfDir.resolve("$sliceDir/Headers"))
                        extraOpts("-libraryPath", xcfDir.resolve(sliceDir).absolutePath)
                    }
                }
            }
            tasks.named(cinterops.getByName("ratex").interopProcessingTaskName) {
                dependsOn(downloadRaTeXXCFramework)
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatformMarkdownRenderer)
            compileOnly(baseLibs.jetbrains.compose.runtime)
            compileOnly(baseLibs.jetbrains.compose.ui)
            compileOnly(baseLibs.jetbrains.compose.foundation)
            implementation(baseLibs.jetbrains.compose.components.resources)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.ratex.android)
        }
        wasmJsMain.dependencies {
            implementation(npm("ratex-wasm", ratexVersion))
        }
    }
}

compose.resources {
    packageOfResClass = "com.mikepenz.markdown.latex.generated.resources"
}
