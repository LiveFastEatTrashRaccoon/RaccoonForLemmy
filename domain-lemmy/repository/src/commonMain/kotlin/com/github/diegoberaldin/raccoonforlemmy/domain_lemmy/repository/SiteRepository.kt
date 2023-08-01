package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.utils.toModel

class SiteRepository(
    private val serviceProvider: ServiceProvider,
) {
    suspend fun getCurrentUser(auth: String): UserModel? {
        val response = serviceProvider.site.getSite(
            auth = auth,
        )
        return response.body()?.myUser?.let {
            val user = it.localUserView.person
            val counts = it.localUserView.counts
            user.toModel().copy(score = counts.toModel())
        }
    }
}
