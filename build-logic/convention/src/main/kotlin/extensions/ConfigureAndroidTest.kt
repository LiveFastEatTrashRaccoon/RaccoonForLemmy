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
                "androidUnitTest", "androidHostTest" -> {
                    dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-test").dependency)
                        implementation(kotlin("test-junit"))
                        implementation(libs.findLibrary("mockk").dependency)
                        implementation(libs.findLibrary("turbine").dependency)
                        implementation(project(":core:testutils"))
                    }
                }
                "commonTest" -> {
                    dependencies {
                        implementation(kotlin("test"))
                    }
                }
            }
        }

        targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
            withHostTest { }
        }
    }
}
