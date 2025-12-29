plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.coil2"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatformMarkdownRenderer)
                compileOnly(compose.runtime)
                compileOnly(compose.ui)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.coil2.core)
            }
        }
    }
}