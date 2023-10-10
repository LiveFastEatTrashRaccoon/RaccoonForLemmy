package com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.utils

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostRepository

internal class CommunityPaginator(
    private val communityId: Int,
    private val postRepository: PostRepository,
) {
    private var currentPage: Int = 1
    var canFetchMore: Boolean = true
        private set

    fun reset() {
        currentPage = 1
        canFetchMore = true
    }

    suspend fun loadNextPage(
        auth: String?,
        sort: SortType,
        currentIds: List<Int>,
    ): List<PostModel> {
        val result = postRepository.getAll(
            auth = auth,
            page = currentPage,
            limit = PostRepository.DEFAULT_PAGE_SIZE,
            type = ListingType.All,
            sort = sort,
            communityId = communityId,
        )?.filter {
            // prevents accidental duplication
            it.id !in currentIds
        }
        if (!result.isNullOrEmpty()) {
            currentPage++
        }
        canFetchMore = result?.isEmpty() != true
        return result.orEmpty()
    }
}
