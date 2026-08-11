plugins {
    id("com.mikepenz.convention.android-application")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.mikepenz.aboutlibraries.plugin.android")
    id("com.mikepenz.convention.composable-preview-scanner.paparazzi-plugin")
}

android {
    namespace = "com.mikepenz.markdown.sample"

    defaultConfig {
        applicationId = "com.mikepenz.markdown"
        base.archivesName = "markdown-renderer-sample-v$versionName-c$versionCode"
    }
}

dependencies {
    implementation(project(":sample:shared"))
    implementation(baseLibs.jetbrains.compose.foundation)
    implementation(baseLibs.jetbrains.compose.material)
    implementation(baseLibs.jetbrains.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ktor.client.okhttp)
    implementation(baseLibs.jetbrains.compose.ui.tooling)
}

aboutLibraries {
    library {
        duplicationMode = com.mikepenz.aboutlibraries.plugin.DuplicateMode.MERGE
    }
    export {
        exportVariant = "release"
    }
}

/*
composablePreviewPaparazzi {
    enable = true
    packages.add("com.mikepenz.markdown.ui")
}
 */

/**
 * Snapshots recorded from `ui/readme/ReadmeShowcasePreviews.kt`, mapped to the stable names the
 * README links to. Re-run after `recordPaparazzi` so the README art follows the current UI:
 *
 *   ./gradlew :sample:android:recordPaparazzi :sample:android:copyReadmeArt
 */
val readmeArt = mapOf(
    "showcaserichtext.light" to "showcase-rich-text-light.png",
    "showcaserichtext.dark_night" to "showcase-rich-text-dark.png",
    "showcasesyntaxhighlighting.light" to "showcase-syntax-light.png",
    "showcasesyntaxhighlighting.dark_night" to "showcase-syntax-dark.png",
    "showcasetablesandalerts.light" to "showcase-tables-alerts-light.png",
    "showcasetablesandalerts.dark_night" to "showcase-tables-alerts-dark.png",
    "showcasecustomcomponents.light" to "showcase-custom-light.png",
    "showcasecustomcomponents.dark_night" to "showcase-custom-dark.png",
).mapKeys { (preview, _) ->
    "Paparazzi_Preview_Test_com.mikepenz.markdown.ui.readme.readmeshowcasepreviewskt.$preview.png"
}

tasks.register<Copy>("copyReadmeArt") {
    group = "documentation"
    description = "Copies the README showcase snapshots into art/ under stable names."

    val snapshots = layout.projectDirectory.dir("src/test/snapshots/images")
    into(rootProject.layout.projectDirectory.dir("art"))
    readmeArt.forEach { (src, dst) -> from(snapshots.file(src)) { rename { dst } } }

    // `from` on a missing file is silently skipped — that is exactly how README art goes stale.
    doFirst {
        val missing = readmeArt.keys.filterNot { snapshots.file(it).asFile.exists() }
        require(missing.isEmpty()) { "Missing README snapshots: ${missing.joinToString()}" }
    }
}