package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.AccountBansModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.AccountSettingsModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.LanguageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MetadataModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

interface SiteRepository {
    suspend fun getCurrentUser(auth: String): UserModel?

    suspend fun getSiteVersion(auth: String? = null, otherInstance: String? = null): String?

    suspend fun block(id: Long, blocked: Boolean, auth: String? = null)

    suspend fun getMetadata(url: String): MetadataModel?

    suspend fun getLanguages(auth: String?): List<LanguageModel>

    suspend fun getAccountSettings(auth: String): AccountSettingsModel?

    suspend fun updateAccountSettings(auth: String, value: AccountSettingsModel)

    suspend fun getBans(auth: String): AccountBansModel?

    suspend fun getAdmins(otherInstance: String? = null): List<UserModel>
}
