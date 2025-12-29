plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.code"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatformMarkdownRenderer)
                compileOnly(compose.runtime)
                compileOnly(compose.ui)
                compileOnly(compose.foundation)

                api(libs.highlights)
            }
        }
    }
}