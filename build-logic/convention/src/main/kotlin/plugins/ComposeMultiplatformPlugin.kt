package plugins

import extensions.configureComposeMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.libs
import utils.pluginId

class ComposeMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("jetbrains-compose").pluginId)
                apply(libs.findPlugin("compose-compiler").pluginId)
            }

            extensions.configure(
                KotlinMultiplatformExtension::class.java,
                ::configureComposeMultiplatform,
            )
        }
}
