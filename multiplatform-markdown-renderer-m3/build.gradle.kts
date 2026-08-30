plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
    id("com.mikepenz.convention.publishing")
}

kotlin {
    android {
        namespace = "com.mikepenz.markdown.m3"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatformMarkdownRenderer)
            api(libs.markdown)

            compileOnly(baseLibs.jetbrains.compose.runtime)
            compileOnly(baseLibs.jetbrains.compose.material3)
        }
        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
            implementation(compose.desktop.currentOs)
            implementation(baseLibs.jetbrains.compose.material3)
            implementation(baseLibs.jetbrains.compose.ui.test)
        }
    }
}
