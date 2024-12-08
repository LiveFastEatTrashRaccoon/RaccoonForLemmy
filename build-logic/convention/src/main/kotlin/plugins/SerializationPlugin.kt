package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs
import utils.pluginId

class SerializationPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").pluginId)
                apply(libs.findPlugin("kotlinx-serialization").pluginId)
            }
            extensions.configure(
                KotlinMultiplatformExtension::class.java,
            ) {
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("kotlinx-serialization-json").dependency)
                        }
                    }
                }
            }
        }
}
