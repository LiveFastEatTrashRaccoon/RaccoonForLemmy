package plugins

import extensions.configureAndroidTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            extensions.configure(
                KotlinMultiplatformExtension::class.java,
                ::configureAndroidTest,
            )
        }
}
