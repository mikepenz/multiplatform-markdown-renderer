package com.mikepenz.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val multiplatformEnabled = project.properties.getOrDefault("org.mikepenz.multiplatform.enabled", "true").toString().toBoolean()
        if (multiplatformEnabled) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
            }

            val targetsEnabled = project.properties.getOrDefault("org.mikepenz.targets.enabled", "true").toString().toBoolean()
            if (targetsEnabled) {
                extensions.configure<KotlinMultiplatformExtension> {
                    configureMultiplatformTargets(target)
                }
            }
        }

        configureJava() // Configure Java to use our chosen language level. Kotlin will automatically pick this up
        configureKotlin()
    }
}

fun KotlinMultiplatformExtension.configureMultiplatformTargets(project: Project) {
    val jvmOnly = project.properties.getOrDefault("org.mikepenz.jvm.only", "false").toString().toBoolean()

    applyDefaultHierarchyTemplate()

    if (project.pluginManager.hasPlugin("com.android.library")) {
        androidTarget {
            publishLibraryVariants("release")
        }
    }
    jvm()

    if (!jvmOnly) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
        }
        js(IR) {
            nodejs()
        }
        macosX64()
        macosArm64()
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }
}

fun Project.configureKotlin() {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            allWarningsAsErrors.set(true)

            if (this is KotlinJvmCompilerOptions) {
                jvmTarget.set(JvmTarget.JVM_17)
            }

            languageVersion.set(KotlinVersion.KOTLIN_2_0)
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
        }
    }
}

fun Project.configureJava() {
    if (extensions.findByType(JavaPluginExtension::class.java) != null) {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
}

private fun Project.java(action: JavaPluginExtension.() -> Unit) = extensions.configure<JavaPluginExtension>(action)
