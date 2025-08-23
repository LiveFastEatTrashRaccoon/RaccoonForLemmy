package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.uniqueIdentifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper

class DefaultExplorePaginationManager(
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val commentRepository: CommentRepository,
    private val communityRepository: CommunityRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val domainBlocklistRepository: DomainBlocklistRepository,
    private val stopWordRepository: StopWordRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val userTagHelper: UserTagHelper,
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

    override suspend fun loadNextPage(): List<SearchResult> {
        val specification = specification ?: return emptyList()
        val auth = identityRepository.authToken.value.orEmpty()
        val accountId = accountRepository.getActive()?.id
        if (blockedDomains == null) {
            blockedDomains = domainBlocklistRepository.get(accountId)
        }
        if (stopWords == null) {
            stopWords = stopWordRepository.get(accountId)
        }

        val searchText = specification.query.orEmpty()
        val type = specification.resultType
        val itemList: List<SearchResult> =
            communityRepository.search(
                query = searchText,
                auth = auth,
                resultType = type,
                page = currentPage,
                listingType = specification.listingType,
                sortType = specification.sortType,
                instance = specification.otherInstance,
            )
        val resolveResults =
            buildList {
                if (currentPage == 1 && searchText.isNotEmpty()) {
                    if (type in listOf(SearchResultType.All, SearchResultType.Communities)) {
                        communityRepository
                            .getResolved(
                                query = searchText,
                                auth = auth,
                            )?.also {
                                add(SearchResult.Community(it))
                            }
                    }

                    if (type in listOf(SearchResultType.All, SearchResultType.Users)) {
                        userRepository
                            .getResolved(
                                query = searchText,
                                auth = auth,
                            )?.also {
                                add(SearchResult.User(it))
                            }
                    }

                    if (type in listOf(SearchResultType.All, SearchResultType.Posts)) {
                        postRepository
                            .getResolved(
                                query = searchText,
                                auth = auth,
                            )?.also {
                                add(SearchResult.Post(it))
                            }
                    }

                    if (type in listOf(SearchResultType.All, SearchResultType.Comments)) {
                        commentRepository
                            .getResolved(
                                query = searchText,
                                auth = auth,
                            )?.also {
                                add(SearchResult.Comment(it))
                            }
                    }
                }
            }

        if (itemList.isNotEmpty()) {
            currentPage++
        }
        canFetchMore = itemList.isNotEmpty()

        val result =
            (resolveResults + itemList)
                .let {
                    when (type) {
                        SearchResultType.Posts -> {
                            if (specification.searchPostTitleOnly && searchText.isNotEmpty()) {
                                // apply the more restrictive title-only search
                                it.filterIsInstance<SearchResult.Post>().filter { res ->
                                    res.model.title.contains(
                                        other = searchText,
                                        ignoreCase = true,
                                    )
                                }
                            } else {
                                it
                            }
                        }

                        SearchResultType.Users -> {
                            if (specification.listingType == ListingType.Local &&
                                specification.restrictLocalUserSearch
                            ) {
                                val referenceHost =
                                    specification.otherInstance?.takeIf { s -> s.isNotEmpty() }
                                        ?: apiConfigurationRepository.instance.value
                                it.filterIsInstance<SearchResult.User>().filter { res ->
                                    res.model.host == referenceHost
                                }
                            } else {
                                it
                            }
                        }

                        else -> it
                    }
                }.deduplicate()
                .filterNsfw(specification.includeNsfw)
                .filterDeleted()
                .filterByUrlDomain()
                .filterByStopWords()
                .withUserTags()

        history.addAll(result)
        // returns a copy of the whole history
        return history.map { it }
    }

    private fun List<SearchResult>.deduplicate(): List<SearchResult> = filter { c1 ->
        // prevents accidental duplication
        history.none { c2 -> c2.uniqueIdentifier == c1.uniqueIdentifier }
    }

    private fun List<SearchResult>.filterNsfw(includeNsfw: Boolean): List<SearchResult> = if (includeNsfw) {
        this
    } else {
        filter { result ->
            when (result) {
                is SearchResult.Community -> !result.model.nsfw
                is SearchResult.Post -> !result.model.nsfw
                is SearchResult.Comment -> true
                is SearchResult.User -> true
            }
        }
    }

    private fun List<SearchResult>.filterDeleted(): List<SearchResult> = filter { result ->
        when (result) {
            is SearchResult.Post -> !result.model.deleted
            is SearchResult.Comment -> !result.model.deleted
            else -> true
        }
    }

    private fun List<SearchResult>.filterByUrlDomain(): List<SearchResult> = filter { result ->
        when (result) {
            is SearchResult.Post -> {
                blockedDomains?.takeIf { l -> l.isNotEmpty() }?.let { blockList ->
                    blockList.none { domain -> result.model.url?.contains(domain) ?: true }
                } ?: true
            }

            else -> true
        }
    }

    private fun List<SearchResult>.filterByStopWords(): List<SearchResult> = filter { result ->
        when (result) {
            is SearchResult.Post -> {
                stopWords?.takeIf { l -> l.isNotEmpty() }?.let { stopWordList ->
                    stopWordList.none { domain ->
                        result.model.title.contains(
                            other = domain,
                            ignoreCase = true,
                        )
                    }
                } ?: true
            }

            else -> true
        }
    }

    private suspend fun List<SearchResult>.withUserTags(): List<SearchResult> = map { result ->
        when (result) {
            is SearchResult.Post ->
                with(userTagHelper) {
                    result.copy(
                        model =
                        result.model.let { post ->
                            post.copy(creator = post.creator.withTags())
                        },
                    )
                }

            is SearchResult.Comment ->
                with(userTagHelper) {
                    result.copy(
                        model =
                        result.model.let { comment ->
                            comment.copy(creator = comment.creator.withTags())
                        },
                    )
                }

            is SearchResult.User ->
                with(userTagHelper) {
                    result.copy(
                        model = result.model.withTags() ?: result.model,
                    )
                }

            else -> result
        }
    }
}
