package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider

internal class DefaultSiteVersionDataSource(
    private val services: ServiceProvider,
    private val customServices: ServiceProvider,
) : SiteVersionDataSource {
    companion object {
        private val LEMMY_VERSION_REGEX = Regex("(?<major>\\d+).(?<minor>\\d+)(.(?<patch>\\d+))?")
    }

    override suspend fun isAtLeast(
        major: Int,
        minor: Int,
        patch: Int,
        otherInstance: String?,
    ): Boolean {
        val version =
            if (otherInstance.isNullOrEmpty()) {
                services.getApiVersion()
            } else {
                customServices.getApiVersion()
            }
        val matchResult = LEMMY_VERSION_REGEX.find(version)
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

    override suspend fun shouldUseV4(otherInstance: String?): Boolean = isAtLeast(major = 1, minor = 0, patch = 0)
}
