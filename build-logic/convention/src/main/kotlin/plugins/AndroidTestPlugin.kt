package plugins

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import extensions.configureTestAndroid
import extensions.configureTestAndroidLibrary
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.libs
import utils.pluginId

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                configureTestAndroid(this)

                targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                    configureTestAndroidLibrary(this)
                }
            }

            pluginManager.withPlugin(libs.findPlugin("kotlinx-kover").pluginId) {
                afterEvaluate {
                    extensions.configure<KoverProjectExtension>("kover") {
                        currentProject {
                            providedVariant("android") {
                                sources {
                                    // ensure commonMain is part of the android variant
                                    includedSourceSets.add("commonMain")
                                    includedSourceSets.add("androidMain")
                                }
                            }
                        }

                        // workaround to include all the classes in the namespace
                        reports {
                            variant("android") {
                                filters {
                                    includes {
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
