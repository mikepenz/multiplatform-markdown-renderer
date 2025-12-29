plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.coil3"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatformMarkdownRenderer)
                api(libs.coil.core.get().toString()) {
                    exclude(group = "org.jetbrains.compose.runtime")
                }
                compileOnly(compose.runtime)
            }
        }
    }
}
