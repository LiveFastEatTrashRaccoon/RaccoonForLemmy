package extensions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs

internal fun Project.configureAndroidTest(extension: KotlinMultiplatformExtension) {
    extension.apply {
        sourceSets.configureEach {
            when (name) {
                "androidHostTest", "androidDeviceTest" -> {
                    dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-test").dependency)
                        implementation(kotlin("test-junit"))
                        implementation(libs.findLibrary("mockk").dependency)
                        implementation(libs.findLibrary("turbine").dependency)
                        implementation(project(":core:testutils"))
                    }
                }
            }
        }

        sourceSets.maybeCreate("androidDeviceTest").dependencies {
            implementation(libs.findLibrary("compose-ui-test").dependency)
            implementation(libs.findLibrary("compose-ui-test-manifest").dependency)
            implementation(libs.findLibrary("androidx-test-ext-junit").dependency)
            implementation(libs.findLibrary("androidx-test-runner").dependency)
            implementation(libs.findLibrary("mockk-android").dependency)
        }

        sourceSets.getByName("commonTest").dependencies {
            implementation(kotlin("test"))
        }

        targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
            withHostTest { }
            withDeviceTest {
                instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }
}
