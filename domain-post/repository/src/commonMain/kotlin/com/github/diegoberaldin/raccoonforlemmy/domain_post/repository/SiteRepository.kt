package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toModel

class SiteRepository(
    private val serviceProvider: ServiceProvider,
) {
    suspend fun getCurrentUser(auth: String): UserModel? {
        val response = serviceProvider.site.getSite(
            auth = auth
        )
        return response.body()?.myUser?.let {
            val user = it.localUserView.person
            val counts = it.localUserView.counts
            user.toModel().copy(score = counts.toModel())
        }
    }
}