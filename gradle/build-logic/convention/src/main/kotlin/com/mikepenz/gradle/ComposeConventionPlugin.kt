package com.mikepenz.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        configureCompose()
    }
}

internal fun Project.configureCompose() {
    composeCompiler {
        reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
        metricsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    }
}

private fun Project.composeCompiler(action: org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension.() -> Unit) =
    extensions.configure<org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension>(action)
