package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider

internal class DefaultSiteVersionDataSource(
    private val services: ServiceProvider,
    private val customServices: ServiceProvider,
) : SiteVersionDataSource {
    companion object {
        private val LEMMY_VERSION_REGEX = Regex("(?<major>\\d+).(?<minor>\\d+)(.(?<patch>\\d+))?")
    }

    private val cache = mutableMapOf<String, String>()

    override suspend fun isAtLeast(major: Int, minor: Int, patch: Int, otherInstance: String?): Boolean {
        val instance = otherInstance ?: services.currentInstance
        val version =
            if (cache.contains(instance)) {
                cache[instance].orEmpty()
            } else {
                retrieveVersion(otherInstance = otherInstance).also {
                    cache[instance] = it
                }
            }

        return determine(
            actualVersion = version,
            major = major,
            minor = minor,
            patch = patch,
        )
    }

    private suspend fun retrieveVersion(otherInstance: String?): String = if (otherInstance.isNullOrEmpty()) {
        services.getApiVersion()
    } else {
        customServices.getApiVersion()
    }

    private fun determine(actualVersion: String, major: Int, minor: Int, patch: Int): Boolean {
        val matchResult = LEMMY_VERSION_REGEX.find(actualVersion)
        val actualMajor =
            matchResult
                ?.groups
                ?.get("major")
                ?.value
                ?.toIntOrNull() ?: 0
        val actualMinor =
            matchResult
                ?.groups
                ?.get("minor")
                ?.value
                ?.toIntOrNull() ?: 0
        val actualPatch =
            matchResult
                ?.groups
                ?.get("patch")
                ?.value
                ?.toIntOrNull() ?: 0
        return when {
            actualMajor < major -> false
            actualMajor > major -> true
            actualMinor < minor -> false
            actualMinor > minor -> true
            actualPatch < patch -> false
            else -> true
        }
    }
}
