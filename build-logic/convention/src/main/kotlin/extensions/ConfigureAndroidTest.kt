package extensions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs

internal fun Project.configureTestAndroid(extension: KotlinMultiplatformExtension) {
    extension.apply {
        sourceSets.apply {
            commonTest {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.findLibrary("kotlinx-coroutines-test").dependency)
                    implementation(libs.findLibrary("turbine").dependency)
                }
            }

            configureEach {
                when (name) {
                    "androidHostTest", "androidDeviceTest" -> {
                        dependencies {
                            implementation(kotlin("test-junit"))
                            implementation(libs.findLibrary("mockk").dependency)
                            implementation(project(":core:testutils"))
                        }
                    }
                }
            }

            maybeCreate("androidDeviceTest").dependencies {
                implementation(libs.findLibrary("compose-ui-test").dependency)
                implementation(libs.findLibrary("compose-ui-test-manifest").dependency)
                implementation(libs.findLibrary("androidx-test-ext-junit").dependency)
                implementation(libs.findLibrary("androidx-test-runner").dependency)
                implementation(libs.findLibrary("mockk-android").dependency)
            }
        }
    }
}

internal fun Project.configureTestAndroidLibrary(extension: KotlinMultiplatformAndroidLibraryExtension) {
    extension.apply {
        withHostTest { }
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}
