package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

interface GetSortTypesUseCase {
    suspend fun getTypesForPosts(otherInstance: String? = null): List<SortType>

    suspend fun getTypesForComments(otherInstance: String? = null): List<SortType>

    suspend fun getTypesForCommunities(otherInstance: String? = null): List<SortType>

    suspend fun getTypesForSavedItems(): List<SortType>
}
