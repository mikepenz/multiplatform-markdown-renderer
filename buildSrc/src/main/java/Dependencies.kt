object Versions {
    const val androidMinSdk = 21
    const val androidCompileSdk = 31
    const val androidTargetSdk = androidCompileSdk

    const val kotlin = "1.5.31"

    const val markdown = "0.2.4"

    const val coil = "1.4.0"
    const val compose = "1.1.0-beta02"

    const val material = "1.4.0"
    const val activityCompose = "1.4.0"
    const val lifecycleKtx = "2.3.1"
}

object Deps {
    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
    }

    object AndroidX {
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    }

    object Markdown {
        const val core = "org.jetbrains:markdown:${Versions.markdown}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"

        const val coilCompose = "io.coil-kt:coil-compose:${Versions.coil}"
    }
}
