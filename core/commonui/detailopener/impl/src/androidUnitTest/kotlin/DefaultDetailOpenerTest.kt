import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.impl.DefaultDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
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
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds

@Ignore("Ignore this tests \"for now\" due to flakiness")
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultDetailOpenerTest {
    @get:Rule
    val dispatcherRule = DispatcherTestRule()

    private val communityRepository = mockk<CommunityRepository>()

    private val identityRepository = mockk<IdentityRepository>()

    private val lemmyItemCache = mockk<LemmyItemCache>(relaxed = true)

    private val navigationCoordinator = mockk<NavigationCoordinator>(relaxed = true)

    private val sut: DetailOpener =
        DefaultDetailOpener(
            navigationCoordinator = navigationCoordinator,
            itemCache = lemmyItemCache,
            identityRepository = identityRepository,
            communityRepository = communityRepository,
            dispatcher = dispatcherRule.dispatcher,
        )

    @Test
    fun whenOpenCommunityDetailOnSameInstance_thenNavigatesAccordingly() = runTest {
        val community = CommunityModel(name = "test", id = 1)

        launch {
            sut.openCommunityDetail(community)
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            lemmyItemCache.putCommunity(community)
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<CommunityDetailScreen>(it)
                },
            )
            identityRepository wasNot Called
            communityRepository wasNot Called
        }
    }

    @Test
    fun whenOpenCommunityDetailOnDifferentInstance_thenNavigatesAccordingly() = runTest {
        val token = "token"
        val communityName = "test"
        val otherInstance = "otherInstance"
        val community = CommunityModel(name = communityName, id = 1, host = otherInstance)
        every { identityRepository.authToken } returns MutableStateFlow(token)
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = any(),
                limit = any(),
                listingType = any(),
                sortType = any(),
                resultType = any(),
            )
        } returns listOf(SearchResult.Community(community))

        launch {
            sut.openCommunityDetail(community, otherInstance)
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            lemmyItemCache.putCommunity(community)
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<CommunityDetailScreen>(it)
                },
            )
            communityRepository.search(
                query = communityName,
                auth = token,
                page = any(),
                limit = any(),
                listingType = ListingType.All,
                sortType = any(),
                resultType = SearchResultType.Communities,
            )
        }
    }

    @Test
    fun whenOpenUserDetail_thenNavigatesAccordingly() = runTest {
        val user = UserModel(name = "test", id = 1)

        launch {
            sut.openUserDetail(user)
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            lemmyItemCache.putUser(user)
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<UserDetailScreen>(it)
                },
            )
        }
    }

    @Test
    fun whenOpenPostDetail_thenNavigatesAccordingly() = runTest {
        val post = PostModel(title = "test", id = 1)

        launch {
            sut.openPostDetail(post)
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            lemmyItemCache.putPost(post)
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<PostDetailScreen>(it)
                },
            )
        }
    }

    @Test
    fun whenOpenReplyToPost_thenNavigatesAccordingly() = runTest {
        val post = PostModel(title = "test", id = 1)

        launch {
            sut.openReply(originalPost = post)
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            lemmyItemCache.putPost(post)
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<CreateCommentScreen>(it)
                },
            )
        }
    }

    @Test
    fun whenOpenReplyToComment_thenNavigatesAccordingly() = runTest {
        val comment = CommentModel(text = "test", id = 1)

        launch {
            sut.openReply(originalComment = comment, originalPost = PostModel(id = 0))
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            lemmyItemCache.putComment(comment)
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<CreateCommentScreen>(it)
                },
            )
        }
    }

    @Test
    fun whenOpenCreatePost_thenNavigatesAccordingly() = runTest {
        launch {
            sut.openCreatePost()
        }
        advanceTimeBy(OPEN_DELAY)

        coVerify {
            navigationCoordinator.pushScreen(
                withArg {
                    assertIs<CreatePostScreen>(it)
                },
            )
        }
    }

    companion object {
        private val OPEN_DELAY = 1.5.seconds
    }
}
