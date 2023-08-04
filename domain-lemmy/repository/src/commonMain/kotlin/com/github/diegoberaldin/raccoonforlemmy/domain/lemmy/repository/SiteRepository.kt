package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class SiteRepository(
    private val serviceProvider: ServiceProvider,
) {
    suspend fun getCurrentUser(auth: String): UserModel? {
        val response = serviceProvider.site.get(
            auth = auth,
        )
        return response.body()?.myUser?.let {
            val user = it.localUserView.person
            val counts = it.localUserView.counts
            user.toModel().copy(score = counts.toModel())
        }
    }
}
