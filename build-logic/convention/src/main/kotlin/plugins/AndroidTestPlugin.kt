package plugins

import extensions.configureAndroidTest
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.libs
import utils.pluginId

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            extensions.configure(
                KotlinMultiplatformExtension::class.java,
                ::configureAndroidTest,
            )

            // Configure Kover 0.9.x for Android KMP
            pluginManager.withPlugin(libs.findPlugin("kotlinx-kover").pluginId) {
                afterEvaluate {
                    extensions.configure<KoverProjectExtension>("kover") {
                        currentProject {
                            providedVariant("android") {
                                sources {
                                    // Ensure commonMain is part of the android variant
                                    includedSourceSets.add("commonMain")
                                    includedSourceSets.add("androidMain")
                                }
                            }
                        }

                        // Configure reports to include your package
                        reports {
                            variant("android") {
                                filters {
                                    includes {
                                        // Use your package name found in constants
                                        packages("com.livefast.eattrash.raccoonforlemmy")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
}
