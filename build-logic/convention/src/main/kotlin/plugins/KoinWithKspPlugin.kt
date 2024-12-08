package plugins

import com.google.devtools.ksp.gradle.KspExtension
import extensions.configureKoinAnnotations
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import utils.dependency
import utils.libs
import utils.pluginId

class KoinWithKspPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").pluginId)
            }

            extensions.configure(
                KotlinMultiplatformExtension::class.java,
                ::configureKoinAnnotations,
            )

            extensions.configure(KspExtension::class.java) {
                arg("KOIN_DEFAULT_MODULE", "false")
            }

            dependencies.apply {
                add("kspCommonMainMetadata", libs.findLibrary("koin-ksp").dependency)
                add("kspAndroid", libs.findLibrary("koin-ksp").dependency)
                add("kspIosX64", libs.findLibrary("koin-ksp").dependency)
                add("kspIosArm64", libs.findLibrary("koin-ksp").dependency)
                add("kspIosSimulatorArm64", libs.findLibrary("koin-ksp").dependency)
            }

            tasks.withType(KotlinCompilationTask::class.java).configureEach {
                if (name != "kspCommonMainKotlinMetadata") {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }
        }
    }
}
