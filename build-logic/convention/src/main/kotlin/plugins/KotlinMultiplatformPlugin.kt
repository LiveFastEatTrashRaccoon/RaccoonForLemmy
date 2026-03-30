package plugins

import extensions.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.libs
import utils.pluginId

class KotlinMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-multiplatform").pluginId)
                apply(libs.findPlugin("android-kmp-library").pluginId)
            }

            extensions.configure(
                KotlinMultiplatformExtension::class.java,
                ::configureKotlinMultiplatform,
            )
        }
}
