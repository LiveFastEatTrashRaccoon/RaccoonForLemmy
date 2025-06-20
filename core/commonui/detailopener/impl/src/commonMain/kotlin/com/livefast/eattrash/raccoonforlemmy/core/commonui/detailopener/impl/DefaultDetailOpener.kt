package com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.impl

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.CommunityDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.CreateCommentScreen
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.CreatePostScreen
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.PostDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.UserDetailScreen
import com.livefast.eattrash.raccoonforlemmy.unit.web.WebViewScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val MAX_PAGE_NUMBER_IN_COMMUNITY_REC_SEARCH = 10
private const val PAGE_SIZE_IN_COMMUNITY_REC_SEARCH = 50

class DefaultDetailOpener(
    private val navigationCoordinator: NavigationCoordinator,
    private val itemCache: LemmyItemCache,
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : DetailOpener {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun openCommunityDetail(community: CommunityModel, otherInstance: String) {
        scope.launch {
            val (actualCommunity, actualInstance) =
                run {
                    val defaultResult = community to otherInstance
                    if (otherInstance.isNotEmpty()) {
                        val found = searchCommunity(name = community.name, host = otherInstance)
                        if (found != null) {
                            found to ""
                        } else {
                            defaultResult
                        }
                    } else {
                        defaultResult
                    }
                }
            itemCache.putCommunity(actualCommunity)
            navigationCoordinator.pushScreen(
                CommunityDetailScreen(
                    communityId = actualCommunity.id,
                    otherInstance = actualInstance,
                ),
            )
        }
    }

    override fun openUserDetail(user: UserModel, otherInstance: String) {
        scope.launch {
            itemCache.putUser(user)
            navigationCoordinator.pushScreen(
                UserDetailScreen(
                    userId = user.id,
                    otherInstance = otherInstance,
                ),
            )
        }
    }

    override fun openPostDetail(post: PostModel, otherInstance: String, highlightCommentId: Long?, isMod: Boolean) {
        scope.launch {
            itemCache.putPost(post)
            navigationCoordinator.pushScreen(
                PostDetailScreen(
                    postId = post.id,
                    highlightCommentId = highlightCommentId,
                    otherInstance = otherInstance,
                    isMod = isMod,
                ),
            )
        }
    }

    override fun openReply(
        draftId: Long?,
        originalPost: PostModel,
        originalComment: CommentModel?,
        editedComment: CommentModel?,
        initialText: String?,
    ) {
        scope.launch {
            itemCache.putPost(originalPost)
            if (originalComment != null) {
                itemCache.putComment(originalComment)
            }
            if (editedComment != null) {
                itemCache.putComment(editedComment)
            }
            val screen =
                CreateCommentScreen(
                    draftId = draftId,
                    originalPostId = originalPost.id,
                    originalCommentId = originalComment?.id,
                    editedCommentId = editedComment?.id,
                    initialText = initialText,
                )
            navigationCoordinator.pushScreen(screen)
        }
    }

    override fun openCreatePost(
        draftId: Long?,
        editedPost: PostModel?,
        crossPost: PostModel?,
        communityId: Long?,
        initialText: String?,
        initialTitle: String?,
        initialUrl: String?,
        initialNsfw: Boolean?,
        forceCommunitySelection: Boolean,
    ) {
        scope.launch {
            if (editedPost != null) {
                itemCache.putPost(editedPost)
            }
            if (crossPost != null) {
                itemCache.putPost(crossPost)
            }
            val screen =
                CreatePostScreen(
                    draftId = draftId,
                    editedPostId = editedPost?.id,
                    crossPostId = crossPost?.id,
                    communityId = communityId,
                    initialText = initialText,
                    initialTitle = initialTitle,
                    initialUrl = initialUrl,
                    initialNsfw = initialNsfw,
                    forceCommunitySelection = forceCommunitySelection,
                )
            navigationCoordinator.pushScreen(screen)
        }
    }

    private suspend fun searchCommunity(name: String, host: String): CommunityModel? {
        val auth = identityRepository.authToken.value

        tailrec suspend fun searchRec(page: Int = 0): CommunityModel? {
            val results =
                communityRepository
                    .search(
                        auth = auth,
                        query = name,
                        resultType = SearchResultType.Communities,
                        page = page,
                        limit = PAGE_SIZE_IN_COMMUNITY_REC_SEARCH,
                    ).filterIsInstance<SearchResult.Community>()

            val found =
                results
                    .firstOrNull {
                        it.model.name == name && it.model.host == host
                    }?.model
            // iterates for no more than a number of pages before giving up
            if (found != null || page >= MAX_PAGE_NUMBER_IN_COMMUNITY_REC_SEARCH) {
                return found
            }

            return searchRec(page + 1)
        }

        // start recursive search
        return searchRec()
    }

    override fun openWebInternal(url: String) {
        val screen = WebViewScreen(url)
        navigationCoordinator.pushScreen(screen)
    }
}
