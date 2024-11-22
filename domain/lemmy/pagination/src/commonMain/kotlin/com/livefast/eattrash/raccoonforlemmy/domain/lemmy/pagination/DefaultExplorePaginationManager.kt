package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.uniqueIdentifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class DefaultExplorePaginationManager(
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val communityRepository: CommunityRepository,
    private val userRepository: UserRepository,
    private val domainBlocklistRepository: DomainBlocklistRepository,
    private val stopWordRepository: StopWordRepository,
) : ExplorePaginationManager {
    override var canFetchMore: Boolean = true

    private var specification: ExplorePaginationSpecification? = null
    private var currentPage: Int = 1
    private val history: MutableList<SearchResult> = mutableListOf()
    private var blockedDomains: List<String>? = null
    private var stopWords: List<String>? = null

    override fun reset(specification: ExplorePaginationSpecification) {
        this.specification = specification
        canFetchMore = true
        currentPage = 1
        history.clear()
        blockedDomains = null
        stopWords = null
    }

    override suspend fun loadNextPage(): List<SearchResult> =
        withContext(Dispatchers.IO) {
            val specification = specification ?: return@withContext emptyList()
            val auth = identityRepository.authToken.value.orEmpty()
            val accountId = accountRepository.getActive()?.id
            if (blockedDomains == null) {
                blockedDomains = domainBlocklistRepository.get(accountId)
            }
            if (stopWords == null) {
                stopWords = stopWordRepository.get(accountId)
            }

            val searchText = specification.query.orEmpty()
            val resultType = specification.resultType
            val itemList: List<SearchResult> =
                communityRepository.search(
                    query = searchText,
                    auth = auth,
                    resultType = resultType,
                    page = currentPage,
                    listingType = specification.listingType,
                    sortType = specification.sortType,
                    instance = specification.otherInstance,
                )
            val additionalResolvedCommunity =
                if (resultType == SearchResultType.All ||
                    resultType == SearchResultType.Communities &&
                    currentPage == 1 &&
                    searchText.isNotEmpty()
                ) {
                    communityRepository.getResolved(
                        query = searchText,
                        auth = auth,
                    )
                } else {
                    null
                }
            val additionalResolvedUser =
                if (resultType == SearchResultType.All ||
                    resultType == SearchResultType.Users &&
                    currentPage == 1 &&
                    searchText.isNotEmpty()
                ) {
                    userRepository.getResolved(
                        query = searchText,
                        auth = auth,
                    )
                } else {
                    null
                }

            if (itemList.isNotEmpty()) {
                currentPage++
            }
            canFetchMore = itemList.isNotEmpty()

            val result =
                itemList
                    .deduplicate()
                    .filterNsfw(specification.includeNsfw)
                    .filterDeleted()
                    .filterByUrlDomain()
                    .filterByStopWords()
                    .let {
                        when (resultType) {
                            SearchResultType.Communities -> {
                                if (additionalResolvedCommunity != null &&
                                    it.none { r ->
                                        r is SearchResult.Community && r.model.id == additionalResolvedCommunity.id
                                    }
                                ) {
                                    it + SearchResult.Community(additionalResolvedCommunity)
                                } else {
                                    it
                                }
                            }

                            SearchResultType.Users -> {
                                if (additionalResolvedUser != null &&
                                    it.none { r ->
                                        r is SearchResult.User && r.model.id == additionalResolvedUser.id
                                    }
                                ) {
                                    it + SearchResult.User(additionalResolvedUser)
                                } else {
                                    it
                                }
                            }

                            SearchResultType.Posts -> {
                                if (specification.searchPostTitleOnly && searchText.isNotEmpty()) {
                                    // apply the more restrictive title-only search
                                    it
                                        .filterIsInstance<SearchResult.Post>()
                                        .filter { r ->
                                            r.model.title.contains(
                                                other = searchText,
                                                ignoreCase = true,
                                            )
                                        }
                                } else {
                                    it
                                }
                            }

                            else -> it
                        }
                    }

            history.addAll(result)
            // returns a copy of the whole history
            history.map { it }
        }

    private fun List<SearchResult>.deduplicate(): List<SearchResult> =
        filter { c1 ->
            // prevents accidental duplication
            history.none { c2 -> c2.uniqueIdentifier == c1.uniqueIdentifier }
        }

    private fun List<SearchResult>.filterNsfw(includeNsfw: Boolean): List<SearchResult> =
        if (includeNsfw) {
            this
        } else {
            filter { res ->
                when (res) {
                    is SearchResult.Community -> !res.model.nsfw
                    is SearchResult.Post -> !res.model.nsfw
                    is SearchResult.Comment -> true
                    is SearchResult.User -> true
                    else -> false
                }
            }
        }

    private fun List<SearchResult>.filterDeleted(): List<SearchResult> =
        filter {
            when (it) {
                is SearchResult.Post -> !it.model.deleted
                is SearchResult.Comment -> !it.model.deleted
                else -> true
            }
        }

    private fun List<SearchResult>.filterByUrlDomain(): List<SearchResult> =
        filter {
            when (it) {
                is SearchResult.Post -> {
                    blockedDomains?.takeIf { l -> l.isNotEmpty() }?.let { blockList ->
                        blockList.none { domain -> it.model.url?.contains(domain) ?: true }
                    } ?: true
                }

                else -> true
            }
        }

    private fun List<SearchResult>.filterByStopWords(): List<SearchResult> =
        filter {
            when (it) {
                is SearchResult.Post -> {
                    stopWords?.takeIf { l -> l.isNotEmpty() }?.let { stopWordList ->
                        stopWordList.none { domain ->
                            it.model.title.contains(
                                other = domain,
                                ignoreCase = true,
                            )
                        }
                    } ?: true
                }

                else -> true
            }
        }
}
