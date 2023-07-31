package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.data.UserCounterModel
import com.github.diegoberaldin.raccoonforlemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.extractHost

class UserRepository(
    private val serviceProvider: ServiceProvider,
) {

    suspend fun getUser(
        id: Int,
        auth: String? = null,
    ): Pair<UserModel, UserCounterModel>? {
        val response = serviceProvider.user.getPersonDetails(
            auth = auth,
            personId = id,
        )
        return response.body()?.let {
            val user = UserModel(
                id = it.personView.person.id,
                name = it.personView.person.name,
                avatar = it.personView.person.avatar,
                host = extractHost(it.personView.person.actorId)
            )
            val counters = UserCounterModel(
                postCount = it.personView.counts.postCount,
                postScore = it.personView.counts.postScore,
                commentCount = it.personView.counts.commentCount,
                commentScore = it.personView.counts.commentScore,
            )
            user to counters
        }
    }
}