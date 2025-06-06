package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository

private const val DEFAULT_PAGE_SIZE = 50

internal class DefaultCommunityPaginationManager(
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
) : CommunityPaginationManager {
    override var canFetchMore: Boolean = true
    override val history: MutableList<CommunityModel> = mutableListOf()

    private var specification: CommunityPaginationSpecification? = null
    private var currentPage: Int = 1

    override fun reset(specification: CommunityPaginationSpecification) {
        this.specification = specification
        history.clear()
        canFetchMore = true
        currentPage = 1
    }

    override suspend fun loadNextPage(): List<CommunityModel> {
        val specification = specification ?: return emptyList()
        val auth = identityRepository.authToken.value.orEmpty()

        val result =
            when (specification) {
                is CommunityPaginationSpecification.Instance -> {
                    val itemList =
                        communityRepository.search(
                            instance = specification.otherInstance,
                            page = currentPage,
                            sortType = specification.sortType,
                            resultType = SearchResultType.Communities,
                            listingType = ListingType.Local,
                            limit = DEFAULT_PAGE_SIZE,
                        )
                    if (itemList.isNotEmpty()) {
                        currentPage++
                    }
                    canFetchMore = itemList.isEmpty() != true
                    itemList
                        .mapNotNull { it as? SearchResult.Community }
                        .map { it.model }
                        .deduplicate()
                }

                is CommunityPaginationSpecification.Subscribed -> {
                    val itemList =
                        communityRepository.search(
                            auth = auth,
                            page = currentPage,
                            sortType = specification.sortType,
                            resultType = SearchResultType.Communities,
                            listingType = ListingType.Subscribed,
                            query = specification.searchText,
                            limit = DEFAULT_PAGE_SIZE,
                        )
                    if (itemList.isNotEmpty()) {
                        currentPage++
                    }
                    canFetchMore = itemList.isEmpty() != true
                    itemList
                        .mapNotNull { it as? SearchResult.Community }
                        .map { it.model }
                        .deduplicate()
                }
            }

        history.addAll(result)
        // returns a copy of the whole history
        return history.map { it }
    }

    override suspend fun fetchAll(): List<CommunityModel> {
        while (canFetchMore) {
            loadNextPage()
        }
        return history.map { it }
    }

    private fun List<CommunityModel>.deduplicate(): List<CommunityModel> =
        filter { p1 ->
            // prevents accidental duplication
            history.none { p2 -> p2.id == p1.id }
        }
}
