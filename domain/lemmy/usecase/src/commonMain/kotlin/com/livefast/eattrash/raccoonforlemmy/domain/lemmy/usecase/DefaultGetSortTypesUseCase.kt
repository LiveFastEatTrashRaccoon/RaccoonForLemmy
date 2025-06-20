package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource

internal class DefaultGetSortTypesUseCase(private val siteVersionDataSource: SiteVersionDataSource) :
    GetSortTypesUseCase {
    companion object {
        private const val THRESHOLD_MAJOR = 0
        private const val THRESHOLD_MINOR = 19
    }

    override suspend fun getTypesForPosts(otherInstance: String?): List<SortType> = buildList {
        val isAtLeastThreshold =
            siteVersionDataSource.isAtLeast(
                major = THRESHOLD_MAJOR,
                minor = THRESHOLD_MINOR,
                otherInstance = otherInstance,
            )
        this += SortType.Active
        this += SortType.Hot
        this += SortType.New
        this += SortType.NewComments
        this += SortType.MostComments
        this += SortType.Old
        if (isAtLeastThreshold) {
            this += SortType.Controversial
            this += SortType.Scaled
        }
        this += SortType.Top.Generic
    }

    override suspend fun getTypesForComments(otherInstance: String?): List<SortType> = buildList {
        val isAtLeastThreshold =
            siteVersionDataSource.isAtLeast(
                major = THRESHOLD_MAJOR,
                minor = THRESHOLD_MINOR,
                otherInstance = otherInstance,
            )
        this += SortType.Hot
        this += SortType.New
        this += SortType.Old
        if (isAtLeastThreshold) {
            this += SortType.Controversial
        }
        this += SortType.Top.Generic
    }

    override suspend fun getTypesForCommunities(otherInstance: String?): List<SortType> = buildList {
        this += SortType.Active
        this += SortType.New
        this += SortType.MostComments
    }

    override suspend fun getTypesForSavedItems(): List<SortType> = buildList {
        this += SortType.Hot
        this += SortType.New
        this += SortType.Old
    }
}
