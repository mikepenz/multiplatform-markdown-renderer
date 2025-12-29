plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.m2"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatformMarkdownRenderer)
                api(libs.markdown)

                compileOnly(compose.runtime)
                compileOnly(compose.material)
            }
        }
    }
}