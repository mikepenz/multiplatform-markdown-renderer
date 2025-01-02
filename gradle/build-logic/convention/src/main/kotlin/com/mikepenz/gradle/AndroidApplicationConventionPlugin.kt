package com.mikepenz.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            configureBaseAndroid()

            android {
                buildFeatures.compose = true

                defaultConfig {
                    versionCode = property("VERSION_CODE").toString().toInt()
                    versionName = property("VERSION_NAME").toString()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    getByName("debug") {
                        signingConfig = signingConfigs.findByName("debug")
                    }

                    getByName("release") {
                        signingConfig = signingConfigs.findByName("release")

                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
                        )
                    }
                }

                packagingOptions {
                    resources.excludes.add("META-INF/licenses/**")
                    resources.excludes.add("META-INF/AL2.0")
                    resources.excludes.add("META-INF/LGPL2.1")
                }
            }
        }
    }
}
