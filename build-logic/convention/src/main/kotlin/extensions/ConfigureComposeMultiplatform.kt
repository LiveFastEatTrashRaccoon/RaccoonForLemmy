package extensions

import org.gradle.api.Project
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs

internal fun Project.configureComposeMultiplatform(extension: KotlinMultiplatformExtension) =
    extension.apply {
        sourceSets.apply {
            commonMain {
                dependencies {
                    implementation(libs.findLibrary("compose-runtime").dependency)
                    implementation(libs.findLibrary("compose-foundation").dependency)
                    implementation(libs.findLibrary("compose-m3").dependency)
                    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").dependency)
                    implementation(libs.findLibrary("compose-navigationevent").dependency)
                }
            }
        }
    }
