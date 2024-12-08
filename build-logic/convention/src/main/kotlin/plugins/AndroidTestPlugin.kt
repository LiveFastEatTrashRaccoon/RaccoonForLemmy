package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            extensions.configure(
                KotlinMultiplatformExtension::class.java,
            ) {
                sourceSets.apply {
                    androidUnitTest {
                        dependencies {
                            implementation(libs.findLibrary("kotlinx-coroutines-test").dependency)
                            implementation(kotlin("test-junit"))
                            implementation(libs.findLibrary("mockk").dependency)
                            implementation(libs.findLibrary("turbine").dependency)
                            implementation(project(":core:testutils"))
                        }
                    }
                    commonTest {
                        dependencies {
                            implementation(kotlin("test"))
                        }
                    }
                }
            }
        }
}
