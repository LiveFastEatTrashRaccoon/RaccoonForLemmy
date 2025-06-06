package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceForm
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.AccountBansModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.AccountSettingsModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.LanguageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MetadataModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.shouldUseV4
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toModel

internal class DefaultSiteRepository(
    private val services: ServiceProvider,
    private val customServices: ServiceProvider,
    private val siteVersionDataSource: SiteVersionDataSource,
) : SiteRepository {
    override suspend fun getCurrentUser(auth: String): UserModel? =
        runCatching {
            val remoteUser =
                if (siteVersionDataSource.shouldUseV4()) {
                    services.v4.account.get(authHeader = auth.toAuthHeader())
                } else {
                    services.v3.site.get(
                        auth = auth,
                        authHeader = auth.toAuthHeader(),
                    ).myUser
                }
            remoteUser?.let {
                val user = it.localUserView?.person
                val counts = it.localUserView?.counts
                user?.toModel()?.copy(score = counts?.toModel())
            }
        }.getOrNull()

    override suspend fun getSiteVersion(
        auth: String?,
        otherInstance: String?,
    ): String? =
        runCatching {
            if (otherInstance.isNullOrEmpty()) {
                val response = services.v3.site.get(authHeader = auth.toAuthHeader())
                response.version.takeIf { !it.isNullOrEmpty() }
            } else {
                customServices.changeInstance(otherInstance)
                val response = customServices.v3.site.get(authHeader = "")
                response.version.takeIf { !it.isNullOrEmpty() }
            }
        }.getOrNull()

    override suspend fun block(
        id: Long,
        blocked: Boolean,
        auth: String?,
    ) {
        val data =
            BlockInstanceForm(
                instanceId = id,
                block = blocked,
            )
        services.v3.site.block(
            authHeader = auth.toAuthHeader(),
            form = data,
        )
    }

    override suspend fun getMetadata(url: String): MetadataModel? =
        runCatching {
            val response = services.v3.post.getSiteMetadata(url = url)
            response.metadata.toModel()
        }.getOrNull()

    override suspend fun getLanguages(auth: String?): List<LanguageModel> =
        runCatching {
            val response =
                services.v3.site.get(
                    auth = auth,
                    authHeader = auth.toAuthHeader(),
                )
            response.allLanguages.map { it.toModel() }
        }.getOrElse { emptyList() }

    override suspend fun getAccountSettings(auth: String): AccountSettingsModel? =
        runCatching {
            val response =
                services.v3.site.get(
                    auth = auth,
                    authHeader = auth.toAuthHeader(),
                )
            response.myUser?.localUserView?.toModel()
        }.getOrNull()

    override suspend fun updateAccountSettings(
        auth: String,
        value: AccountSettingsModel,
    ) {
        val formData = value.toDto().copy(auth = auth)
        services.v3.user.saveUserSettings(
            authHeader = auth.toAuthHeader(),
            form = formData,
        )
    }

    override suspend fun getBans(auth: String): AccountBansModel? =
        runCatching {
            val response =
                services.v3.site.get(
                    auth = auth,
                    authHeader = auth.toAuthHeader(),
                )
            response.myUser?.run {
                AccountBansModel(
                    users = personBlocks.map { it.target.toModel() },
                    communities = communityBlocks.map { it.community.toModel() },
                    instances = instanceBlocks.map { it.instance.toModel() },
                )
            }
        }.getOrNull()

    override suspend fun getAdmins(otherInstance: String?): List<UserModel> =
        runCatching {
            if (otherInstance.isNullOrEmpty()) {
                val response = services.v3.site.get()
                response.admins.map { it.toModel() }
            } else {
                customServices.changeInstance(otherInstance)
                val response = customServices.v3.site.get()
                response.admins.map { it.toModel() }
            }
        }.getOrElse { emptyList() }
}
