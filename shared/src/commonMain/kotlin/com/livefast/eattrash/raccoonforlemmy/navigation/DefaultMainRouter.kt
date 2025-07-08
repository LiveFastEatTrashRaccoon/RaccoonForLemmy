package com.livefast.eattrash.raccoonforlemmy.navigation

import com.livefast.eattrash.raccoonforlemmy.core.navigation.Destination
import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
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
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.toInt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DefaultMainRouter(
    private val navigationCoordinator: NavigationCoordinator,
    private val itemCache: LemmyItemCache,
    private val communityRepository: CommunityRepository,
    private val identityRepository: IdentityRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : MainRouter {
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    override fun openAccountSettings() {
        navigationCoordinator.push(Destination.AccountSettings)
    }

    override fun openAcknowledgements() {
        navigationCoordinator.push(Destination.Acknowledgements)
    }

    override fun openAdvancedSettings() {
        navigationCoordinator.push(Destination.AdvancedSettings)
    }

    override fun openBanUser(userId: Long, communityId: Long, newValue: Boolean, postId: Long?, commentId: Long?) {
        navigationCoordinator.push(
            Destination.BanUser(
                userId = userId,
                communityId = communityId,
                newValue = newValue,
                postId = postId,
                commentId = commentId,
            ),
        )
    }

    override fun openBookmarks() {
        navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Bookmarks.toInt()))
    }

    override fun openChat(otherUserId: Long) {
        navigationCoordinator.push(Destination.Chat(otherUserId))
    }

    override fun openColorAndFont() {
        navigationCoordinator.push(Destination.ColorAndFont)
    }

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
            navigationCoordinator.push(
                Destination.CommunityDetail(
                    id = actualCommunity.id,
                    otherInstance = actualInstance,
                ),
            )
        }
    }

    override fun openConfigureContentView() {
        navigationCoordinator.push(Destination.ConfigureContentView)
    }

    override fun openConfigureNavBar() {
        navigationCoordinator.push(Destination.ConfigureNavBar)
    }

    override fun openConfigureSwipeActions() {
        navigationCoordinator.push(Destination.ConfigureSwipeActions)
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
            navigationCoordinator.push(
                Destination.CreatePost(
                    draftId = draftId,
                    editedPostId = editedPost?.id,
                    crossPostId = crossPost?.id,
                    communityId = communityId,
                    initialText = initialText,
                    initialTitle = initialTitle,
                    initialUrl = initialUrl,
                    initialNsfw = initialNsfw,
                    forceCommunitySelection = forceCommunitySelection,
                ),
            )
        }
    }

    override fun openDrafts() {
        navigationCoordinator.push(Destination.Drafts)
    }

    override fun openEditCommunity(id: Long?) {
        navigationCoordinator.push(Destination.EditCommunity(id))
    }

    override fun openEditMultiCommunity(id: Long?) {
        navigationCoordinator.push(
            Destination.MultiCommunityEditor(id = id),
        )
    }

    override fun openExplore(otherInstance: String) {
        navigationCoordinator.push(Destination.Explore(otherInstance))
    }

    override fun openHidden() {
        navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Hidden.toInt()))
    }

    override fun openImage(url: String, source: String, isVideo: Boolean) {
        navigationCoordinator.push(
            Destination.ZoomableImage(
                url = url,
                source = source,
                isVideo = isVideo,
            ),
        )
    }

    override fun openInstanceInfo(url: String) {
        navigationCoordinator.push(Destination.InstanceInfo(url))
    }

    override fun openLicences() {
        navigationCoordinator.push(Destination.Licences)
    }

    override fun openLogin() {
        navigationCoordinator.push(Destination.Login)
    }

    override fun openManageSubscriptions() {
        navigationCoordinator.push(Destination.ManageSubscriptions)
    }

    override fun openMediaList() {
        navigationCoordinator.push(Destination.MediaList)
    }

    override fun openModeratedContents() {
        navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Moderated.toInt()))
    }

    override fun openModerateWithReason(actionId: Int, contentId: Long) {
        navigationCoordinator.push(
            Destination.ModerateWithReason(
                actionId = actionId,
                contentId = contentId,
            ),
        )
    }

    override fun openModlog(communityId: Long?) {
        navigationCoordinator.push(Destination.Modlog(communityId))
    }

    override fun openMultiCommunity(id: Long) {
        navigationCoordinator.push(Destination.MultiCommunity(id))
    }

    override fun openManageBans() {
        navigationCoordinator.push(Destination.ManageBans)
    }

    override fun openPostDetail(post: PostModel, otherInstance: String, highlightCommentId: Long?, isMod: Boolean) {
        scope.launch {
            itemCache.putPost(post)
            navigationCoordinator.push(
                Destination.PostDetail(
                    id = post.id,
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
            navigationCoordinator.push(
                Destination.CreateComment(
                    draftId = draftId,
                    originalPostId = originalPost.id,
                    originalCommentId = originalComment?.id,
                    editedCommentId = editedComment?.id,
                    initialText = initialText,
                ),
            )
        }
    }

    override fun openReports(communityId: Long?) {
        navigationCoordinator.push(Destination.ReportList(communityId))
    }

    override fun openSettings() {
        navigationCoordinator.push(Destination.Settings)
    }

    override fun openUserDetail(user: UserModel, otherInstance: String) {
        scope.launch {
            itemCache.putUser(user)
            navigationCoordinator.push(
                Destination.UserDetail(
                    id = user.id,
                    otherInstance = otherInstance,
                ),
            )
        }
    }

    override fun openUserTagDetail(id: Long) {
        navigationCoordinator.push(Destination.UserTagDetail(id))
    }

    override fun openUserTags() {
        navigationCoordinator.push(Destination.UserTags)
    }

    override fun openVotes() {
        navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Votes.toInt()))
    }

    override fun openWebInternal(url: String) {
        navigationCoordinator.push(Destination.WebInternal(url))
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
}

private const val MAX_PAGE_NUMBER_IN_COMMUNITY_REC_SEARCH = 10
private const val PAGE_SIZE_IN_COMMUNITY_REC_SEARCH = 50
