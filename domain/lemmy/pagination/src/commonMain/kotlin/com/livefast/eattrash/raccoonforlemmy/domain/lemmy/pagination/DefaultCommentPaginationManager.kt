package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class DefaultCommentPaginationManager(
    private val identityRepository: IdentityRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val userTagHelper: UserTagHelper,
    notificationCenter: NotificationCenter,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CommentPaginationManager {
    override var canFetchMore: Boolean = true

    private var specification: CommentPaginationSpecification? = null
    private var currentPage: Int = 1
    private val history: MutableList<CommentModel> = mutableListOf()
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        notificationCenter
            .subscribe(NotificationCenterEvent.CommentUpdated::class)
            .onEach { evt ->
                handleCommentUpdate(evt.model)
            }.launchIn(scope)
    }

    override fun reset(specification: CommentPaginationSpecification) {
        this.specification = specification
        history.clear()
        canFetchMore = true
        currentPage = 1
    }

    override suspend fun loadNextPage(): List<CommentModel> {
        val specification = specification ?: return emptyList()
        val auth = identityRepository.authToken.value.orEmpty()

        val result =
            when (specification) {
                is CommentPaginationSpecification.Replies -> {
                    val itemList =
                        commentRepository.getAll(
                            postId = specification.postId,
                            auth = auth,
                            instance = specification.otherInstance,
                            page = currentPage,
                            type = specification.listingType ?: ListingType.All,
                            sort = specification.sortType,
                        )
                    if (!itemList.isNullOrEmpty()) {
                        currentPage++
                    }
                    canFetchMore = itemList?.isEmpty() != true
                    itemList
                        .orEmpty()
                        .deduplicate()
                        .filterDeleted(includeCurrentCreator = specification.includeDeleted)
                        .also {
                            // deleted comments should not be counted
                            canFetchMore = it.isNotEmpty()
                        }.withUserTags()
                }

                is CommentPaginationSpecification.User -> {
                    val itemList =
                        userRepository.getComments(
                            id = specification.id,
                            auth = auth,
                            page = currentPage,
                            sort = specification.sortType,
                            username = specification.name,
                            otherInstance = specification.otherInstance,
                        )
                    if (!itemList.isNullOrEmpty()) {
                        currentPage++
                    }
                    canFetchMore = itemList?.isEmpty() != true
                    itemList
                        .orEmpty()
                        .deduplicate()
                        .filterDeleted(includeCurrentCreator = specification.includeDeleted)
                        .also {
                            canFetchMore = it.isNotEmpty()
                        }.withUserTags()
                }

                is CommentPaginationSpecification.Votes -> {
                    val itemList =
                        userRepository.getLikedComments(
                            auth = auth,
                            page = currentPage,
                            sort = specification.sortType,
                            liked = specification.liked,
                        )
                    if (!itemList.isNullOrEmpty()) {
                        currentPage++
                    }
                    canFetchMore = itemList?.isEmpty() != true
                    itemList
                        .orEmpty()
                        .deduplicate()
                        .filterDeleted(includeCurrentCreator = true)
                        .also {
                            canFetchMore = it.isNotEmpty()
                        }.withUserTags()
                }

                is CommentPaginationSpecification.Saved -> {
                    val itemList =
                        userRepository.getSavedComments(
                            auth = auth,
                            page = currentPage,
                            sort = specification.sortType,
                            id = identityRepository.cachedUser?.id ?: 0,
                        )
                    if (!itemList.isNullOrEmpty()) {
                        currentPage++
                    }
                    canFetchMore = itemList?.isEmpty() != true
                    itemList
                        .orEmpty()
                        .deduplicate()
                        .filterDeleted()
                        .also {
                            canFetchMore = it.isNotEmpty()
                        }.withUserTags()
                }
            }

        history.addAll(result)
        // returns a copy of the whole history
        return history.map { it }
    }

    private fun List<CommentModel>.deduplicate(): List<CommentModel> = filter { c1 ->
        // prevents accidental duplication
        history.none { c2 -> c2.id == c1.id }
    }

    private fun List<CommentModel>.filterDeleted(includeCurrentCreator: Boolean = false): List<CommentModel> {
        val currentUserId = identityRepository.cachedUser?.id
        return filter { comment ->
            !comment.deleted || (includeCurrentCreator && comment.creator?.id == currentUserId)
        }
    }

    private suspend fun List<CommentModel>.withUserTags(): List<CommentModel> = map {
        with(userTagHelper) {
            it.copy(
                creator = it.creator.withTags(),
            )
        }
    }

    private fun handleCommentUpdate(comment: CommentModel) {
        val index = history.indexOfFirst { it.id == comment.id }.takeIf { it >= 0 } ?: return
        history.removeAt(index)
        history.add(index, comment)
    }
}
