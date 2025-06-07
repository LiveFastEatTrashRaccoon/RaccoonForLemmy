package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.diffplug.gradle.spotless.SpotlessExtension
import utils.dependency
import utils.libs
import utils.pluginId

class SpotlessPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("spotless").pluginId)
            }
            extensions.configure(
                SpotlessExtension::class.java,
            ) {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")
                    ktlint(libs.findVersion("ktlint").get().requiredVersion)
                        .editorConfigOverride(
                            mapOf(
                                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                            ),
                        )
                        .customRuleSets(
                            listOf(
                                "io.nlopez.compose.rules:ktlint:0.4.22",
                            ),
                        )
                    trimTrailingWhitespace()
                    endWithNewline()
                }
                kotlinGradle {
                    target("*.gradle.kts")
                    ktlint(libs.findVersion("ktlint").get().requiredVersion)
                }
            }
        }
}
