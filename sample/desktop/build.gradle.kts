import org.gradle.kotlin.dsl.sourceSets
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.mikepenz.convention.kotlin-multiplatform")
    id("com.mikepenz.convention.compose")
}

kotlin {
    sourceSets {
        jvmMain.dependencies {
            implementation(project(":sample:shared"))
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.java)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.mikepenz.markdown.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.mikepenz.markdown"
            packageVersion = "1.0.0"
        }
    }
}
