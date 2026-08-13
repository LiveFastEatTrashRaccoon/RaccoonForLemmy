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
import kotlinx.coroutines.CancellationException

internal class DefaultSiteRepository(
    private val services: ServiceProvider,
    private val customServices: ServiceProvider,
    private val siteVersionDataSource: SiteVersionDataSource,
) : SiteRepository {
    override suspend fun getCurrentUser(auth: String): UserModel? = try {
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
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getSiteVersion(auth: String?, otherInstance: String?): String? = try {
        if (otherInstance.isNullOrEmpty()) {
            val response = services.v3.site.get(authHeader = auth.toAuthHeader())
            response.version.takeIf { !it.isNullOrEmpty() }
        } else {
            customServices.changeInstance(otherInstance)
            val response = customServices.v3.site.get(authHeader = "")
            response.version.takeIf { !it.isNullOrEmpty() }
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun block(id: Long, blocked: Boolean, auth: String?) {
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

    override suspend fun getMetadata(url: String): MetadataModel? = try {
        val response = services.v3.post.getSiteMetadata(url = url)
        response.metadata.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getLanguages(auth: String?): List<LanguageModel> = try {
        val response =
            services.v3.site.get(
                auth = auth,
                authHeader = auth.toAuthHeader(),
            )
        response.allLanguages.map { it.toModel() }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        emptyList()
    }

    override suspend fun getAccountSettings(auth: String): AccountSettingsModel? = try {
        val response =
            services.v3.site.get(
                auth = auth,
                authHeader = auth.toAuthHeader(),
            )
        response.myUser?.localUserView?.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun updateAccountSettings(auth: String, value: AccountSettingsModel) {
        val formData = value.toDto().copy(auth = auth)
        services.v3.user.saveUserSettings(
            authHeader = auth.toAuthHeader(),
            form = formData,
        )
    }

    override suspend fun getBans(auth: String): AccountBansModel? = try {
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
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getAdmins(otherInstance: String?): List<UserModel> = try {
        if (otherInstance.isNullOrEmpty()) {
            val response = services.v3.site.get()
            response.admins.map { it.toModel() }
        } else {
            customServices.changeInstance(otherInstance)
            val response = customServices.v3.site.get()
            response.admins.map { it.toModel() }
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        emptyList()
    }
}
