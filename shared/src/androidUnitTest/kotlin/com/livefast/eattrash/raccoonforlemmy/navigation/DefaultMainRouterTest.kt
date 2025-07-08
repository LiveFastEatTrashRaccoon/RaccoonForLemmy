package com.livefast.eattrash.raccoonforlemmy.navigation

import com.livefast.eattrash.raccoonforlemmy.core.navigation.Destination
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.toInt
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class DefaultMainRouterTest {
    @get:Rule
    val rule = DispatcherTestRule()

    private val navigationCoordinator = mockk<NavigationCoordinator>(relaxUnitFun = true)
    private val itemCache = mockk<LemmyItemCache>(relaxUnitFun = true)
    private val communityRepository = mockk<CommunityRepository>()
    private val identityRepository = mockk<IdentityRepository>()

    private val sut = DefaultMainRouter(
        navigationCoordinator = navigationCoordinator,
        itemCache = itemCache,
        communityRepository = communityRepository,
        identityRepository = identityRepository,
        dispatcher = rule.dispatcher,
    )

    @Test
    fun whenOpenAccountSettings_thenNavigatesAccordingly() = runTest {
        sut.openAccountSettings()

        verify {
            navigationCoordinator.push(Destination.AccountSettings)
        }
    }

    @Test
    fun whenOpenAcknowledgements_thenNavigatesAccordingly() = runTest {
        sut.openAcknowledgements()

        verify {
            navigationCoordinator.push(Destination.Acknowledgements)
        }
    }

    @Test
    fun whenOpenAdvancedSettings_thenNavigatesAccordingly() = runTest {
        sut.openAdvancedSettings()

        verify {
            navigationCoordinator.push(Destination.AdvancedSettings)
        }
    }

    @Test
    fun whenOpenBanUser_thenNavigatesAccordingly() = runTest {
        val userId = 1L
        val communityId = 2L
        val banned = true

        sut.openBanUser(
            userId = userId,
            communityId = communityId,
            newValue = banned,
        )

        verify {
            navigationCoordinator.push(
                Destination.BanUser(
                    userId = userId,
                    communityId = communityId,
                    newValue = banned,
                ),
            )
        }
    }

    @Test
    fun whenOpenBookmarks_thenNavigatesAccordingly() = runTest {
        sut.openBookmarks()

        verify {
            navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Bookmarks.toInt()))
        }
    }

    @Test
    fun whenOpenChat_thenNavigatesAccordingly() = runTest {
        val userId = 1L
        sut.openChat(userId)

        verify {
            navigationCoordinator.push(Destination.Chat(userId))
        }
    }

    @Test
    fun whenOpenColorAndFont_thenNavigatesAccordingly() = runTest {
        sut.openColorAndFont()

        verify {
            navigationCoordinator.push(Destination.ColorAndFont)
        }
    }

    @Test
    fun whenOpenCommunityDetail_thenNavigatesAccordingly() = runTest {
        val id = 1L
        val community = CommunityModel(id = id)

        sut.openCommunityDetail(community)

        coVerify {
            itemCache.putCommunity(community)
            navigationCoordinator.push(Destination.CommunityDetail(id))
        }
    }

    @Test
    fun whenOpenConfigureContentView_thenNavigatesAccordingly() = runTest {
        sut.openConfigureContentView()

        verify {
            navigationCoordinator.push(Destination.ConfigureContentView)
        }
    }

    @Test
    fun whenOpenConfigureNavBar_thenNavigatesAccordingly() = runTest {
        sut.openConfigureNavBar()

        verify {
            navigationCoordinator.push(Destination.ConfigureNavBar)
        }
    }

    @Test
    fun whenOpenConfigureSwipeActions_thenNavigatesAccordingly() = runTest {
        sut.openConfigureSwipeActions()

        verify {
            navigationCoordinator.push(Destination.ConfigureSwipeActions)
        }
    }

    @Test
    fun whenOpenCreatePost_thenNavigatesAccordingly() = runTest {
        sut.openCreatePost()

        verify {
            navigationCoordinator.push(Destination.CreatePost())
        }
    }

    @Test
    fun whenOpenDrafts_thenNavigatesAccordingly() = runTest {
        sut.openDrafts()

        verify {
            navigationCoordinator.push(Destination.Drafts)
        }
    }

    @Test
    fun whenOpenEditMultiCommunity_thenNavigatesAccordingly() = runTest {
        val id = 1L

        sut.openEditMultiCommunity(id)

        verify {
            navigationCoordinator.push(Destination.MultiCommunityEditor(id))
        }
    }

    @Test
    fun whenOpenEditCommunity_thenNavigatesAccordingly() = runTest {
        sut.openEditCommunity()

        verify {
            navigationCoordinator.push(Destination.EditCommunity())
        }
    }

    @Test
    fun whenOpenExplore_thenNavigatesAccordingly() = runTest {
        val otherInstance = "fake-instance"

        sut.openExplore(otherInstance)

        verify {
            navigationCoordinator.push(Destination.Explore(otherInstance))
        }
    }

    @Test
    fun whenOpenModlog_thenNavigatesAccordingly() = runTest {
        sut.openModlog()

        verify {
            navigationCoordinator.push(Destination.Modlog())
        }
    }

    @Test
    fun whenOpenReports_thenNavigatesAccordingly() = runTest {
        sut.openReports()

        verify {
            navigationCoordinator.push(Destination.ReportList())
        }
    }

    @Test
    fun whenOpenHidden_thenNavigatesAccordingly() = runTest {
        sut.openHidden()

        verify {
            navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Hidden.toInt()))
        }
    }

    @Test
    fun whenOpenImage_thenNavigatesAccordingly() = runTest {
        val url = "fake-url"

        sut.openImage(url)

        verify {
            navigationCoordinator.push(Destination.ZoomableImage(url))
        }
    }

    @Test
    fun whenOpenInstanceInfo_thenNavigatesAccordingly() = runTest {
        val url = "fake-url"

        sut.openInstanceInfo(url)

        verify {
            navigationCoordinator.push(Destination.InstanceInfo(url))
        }
    }

    @Test
    fun whenOpenLicences_thenNavigatesAccordingly() = runTest {
        sut.openLicences()

        verify {
            navigationCoordinator.push(Destination.Licences)
        }
    }

    @Test
    fun whenOpenLogin_thenNavigatesAccordingly() = runTest {
        sut.openLogin()

        verify {
            navigationCoordinator.push(Destination.Login)
        }
    }

    @Test
    fun whenOpenManageBans_thenNavigatesAccordingly() = runTest {
        sut.openManageBans()

        verify {
            navigationCoordinator.push(Destination.ManageBans)
        }
    }

    @Test
    fun whenOpenManageSubscriptions_thenNavigatesAccordingly() = runTest {
        sut.openManageSubscriptions()

        verify {
            navigationCoordinator.push(Destination.ManageSubscriptions)
        }
    }

    @Test
    fun whenOpenMediaList_thenNavigatesAccordingly() = runTest {
        sut.openMediaList()

        verify {
            navigationCoordinator.push(Destination.MediaList)
        }
    }

    @Test
    fun whenOpenModerateWithReason_thenNavigatesAccordingly() = runTest {
        val actionId = 1
        val contentId = 2L

        sut.openModerateWithReason(actionId = actionId, contentId = contentId)

        verify {
            navigationCoordinator.push(Destination.ModerateWithReason(actionId = actionId, contentId = contentId))
        }
    }

    @Test
    fun whenOpenModeratedContents_thenNavigatesAccordingly() = runTest {
        sut.openModeratedContents()

        verify {
            navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Moderated.toInt()))
        }
    }

    @Test
    fun whenOpenMultiCommunity_thenNavigatesAccordingly() = runTest {
        val id = 1L

        sut.openMultiCommunity(id)

        verify {
            navigationCoordinator.push(Destination.MultiCommunity(id))
        }
    }

    @Test
    fun whenOpenPostDetail_thenNavigatesAccordingly() = runTest {
        val id = 1L
        val post = PostModel(id = id)

        sut.openPostDetail(post)

        coVerify {
            itemCache.putPost(post)
            navigationCoordinator.push(Destination.PostDetail(id))
        }
    }

    @Test
    fun whenOpenReply_thenNavigatesAccordingly() = runTest {
        val id = 1L
        val post = PostModel(id = id)

        sut.openReply(originalPost = post)

        verify {
            navigationCoordinator.push(Destination.CreateComment(originalPostId = id))
        }
    }

    @Test
    fun whenOpenSettings_thenNavigatesAccordingly() = runTest {
        sut.openSettings()

        verify {
            navigationCoordinator.push(Destination.Settings)
        }
    }

    @Test
    fun whenOpenUserDetail_thenNavigatesAccordingly() = runTest {
        val id = 1L
        val user = UserModel(id = id)

        sut.openUserDetail(user)

        coVerify {
            itemCache.putUser(user)
            navigationCoordinator.push(Destination.UserDetail(id))
        }
    }

    @Test
    fun whenOpenUserTagDetail_thenNavigatesAccordingly() = runTest {
        val id = 1L

        sut.openUserTagDetail(id)

        verify {
            navigationCoordinator.push(Destination.UserTagDetail(id))
        }
    }

    @Test
    fun whenOpenUserTags_thenNavigatesAccordingly() = runTest {
        sut.openUserTags()

        verify {
            navigationCoordinator.push(Destination.UserTags)
        }
    }

    @Test
    fun whenOpenVotes_thenNavigatesAccordingly() = runTest {
        sut.openVotes()

        verify {
            navigationCoordinator.push(Destination.FilteredContents(FilteredContentsType.Votes.toInt()))
        }
    }

    @Test
    fun whenOpenWebInternal_thenNavigatesAccordingly() = runTest {
        val url = "fake-url"

        sut.openWebInternal(url)

        verify {
            navigationCoordinator.push(Destination.WebInternal(url))
        }
    }
}
