package extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.dependency
import utils.libs

internal fun Project.configureKoinAnnotations(extension: KotlinMultiplatformExtension) =
    extension.apply {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        sourceSets.apply {
            commonMain {
                dependencies {
                    implementation(libs.findLibrary("koin-core").dependency)
                    api(libs.findLibrary("koin-annotations").dependency)
                }

                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }
        }
    }
