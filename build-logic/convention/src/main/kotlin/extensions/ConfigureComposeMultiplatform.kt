package extensions

import org.gradle.api.Project
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs

internal fun Project.configureComposeMultiplatform(extension: KotlinMultiplatformExtension) =
    extension.apply {
        val composeDeps = extensions.getByType(ComposePlugin.Dependencies::class.java)
        sourceSets.apply {
            commonMain {
                dependencies {
                    implementation(composeDeps.runtime)
                    implementation(composeDeps.foundation)
                    implementation(composeDeps.material3)
                    implementation(composeDeps.materialIconsExtended)

                    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").dependency)
                }
            }
        }
    }
