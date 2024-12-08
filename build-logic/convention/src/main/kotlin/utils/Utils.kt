package utils

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency
import java.util.Optional

internal val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Optional<Provider<PluginDependency>>.pluginId get() = get().get().pluginId

internal val Optional<VersionConstraint>.version get() = get().requiredVersion.toInt()

internal val Optional<Provider<MinimalExternalModuleDependency>>.dependency get() = get().get()
