package com.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import kotlinx.coroutines.flow.StateFlow

interface PostNavigationManager {
    val canNavigate: StateFlow<Boolean>

    fun push(state: PostPaginationManagerState)

    fun pop()

    suspend fun getPrevious(postId: Long): PostModel?

    suspend fun getNext(postId: Long): PostModel?
}
