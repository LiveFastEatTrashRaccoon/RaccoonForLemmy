package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toHost
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toModel

class UserRepository(
    private val serviceProvider: ServiceProvider,
) {

    suspend fun getUser(
        id: Int,
        auth: String? = null,
    ): UserModel? {
        val response = serviceProvider.user.getPersonDetails(
            auth = auth,
            personId = id,
        )
        return response.body()?.let {
            UserModel(
                id = it.personView.person.id,
                name = it.personView.person.name,
                avatar = it.personView.person.avatar,
                score = it.personView.counts.toModel(),
                host = it.personView.person.actorId.toHost(),
            )
        }
    }
}