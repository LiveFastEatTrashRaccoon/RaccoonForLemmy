package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultExplorePaginationManagerTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val identityRepository: IdentityRepository =
        mockk {
            every { authToken } returns MutableStateFlow(AUTH_TOKEN)
            every { cachedUser } returns UserModel(id = 1)
        }
    private val commentRepository: CommentRepository =
        mockk(relaxUnitFun = true) {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val communityRepository: CommunityRepository =
        mockk(relaxUnitFun = true) {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val postRepository: PostRepository =
        mockk(relaxUnitFun = true) {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val userRepository: UserRepository =
        mockk(relaxUnitFun = true) {
            coEvery { getResolved(any(), any()) } returns null
        }
    private val accountRepository =
        mockk<AccountRepository>(relaxUnitFun = true) {
            coEvery { getActive() } returns null
        }
    private val domainBlocklistRepository =
        mockk<DomainBlocklistRepository>(relaxUnitFun = true) {
            coEvery { get(accountId = any()) } returns emptyList()
        }
    private val stopWordRepository =
        mockk<StopWordRepository>(relaxUnitFun = true) {
            coEvery { get(accountId = any()) } returns emptyList()
        }
    private val userTagHelper =
        mockk<UserTagHelper> {
            coEvery { any<UserModel>().withTags() } answers { firstArg() }
        }
    private val apiConfigurationRepository =
        mockk<ApiConfigurationRepository> {
            every { instance } returns MutableStateFlow("instance")
        }

    private val sut =
        DefaultExplorePaginationManager(
            identityRepository = identityRepository,
            accountRepository = accountRepository,
            commentRepository = commentRepository,
            communityRepository = communityRepository,
            postRepository = postRepository,
            userRepository = userRepository,
            domainBlocklistRepository = domainBlocklistRepository,
            stopWordRepository = stopWordRepository,
            apiConfigurationRepository = apiConfigurationRepository,
            userTagHelper = userTagHelper,
        )

    @Test
    fun whenReset_thenCanFetchMore() = runTest {
        val specification = ExplorePaginationSpecification()
        sut.reset(specification)

        assertTrue(sut.canFetchMore)
    }

    @Test
    fun givenNoResults_whenLoadNextPage_thenResultIsAsExpected() = runTest {
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = any(),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } returns emptyList()
        val specification = ExplorePaginationSpecification()
        sut.reset(specification)

        val items = sut.loadNextPage()

        assertTrue(items.isEmpty())
        coVerify {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = "",
            )
        }
    }

    @Test
    fun givenResults_whenLoadNextPage_thenResultIsAsExpected() = runTest {
        val page = slot<Int>()
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = capture(page),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } answers {
            val pageNumber = page.captured
            if (pageNumber == 1) {
                (0..<20).map { idx ->
                    SearchResult.Post(PostModel(id = idx.toLong()))
                }
            } else {
                emptyList()
            }
        }
        val specification = ExplorePaginationSpecification()
        sut.reset(specification)

        val items = sut.loadNextPage()

        assertEquals(20, items.size)
        assertTrue(sut.canFetchMore)

        coVerify {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = "",
            )
        }
    }

    @Test
    fun givenResults_whenSecondLoadNextPage_thenResultIsAsExpected() = runTest {
        val page = slot<Int>()
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = capture(page),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } answers {
            val pageNumber = page.captured
            if (pageNumber == 1) {
                (0..<20).map { idx ->
                    SearchResult.Post(PostModel(id = idx.toLong()))
                }
            } else {
                emptyList()
            }
        }
        val specification = ExplorePaginationSpecification()
        sut.reset(specification)

        sut.loadNextPage()
        val items = sut.loadNextPage()

        assertEquals(20, items.size)
        assertFalse(sut.canFetchMore)

        coVerifySequence {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = "",
            )

            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 2,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = "",
            )
        }
    }

    @Test
    fun givenResultsAndSearchQuery_whenLoadNextPage_thenResultIsAsExpected() = runTest {
        val query = "text"
        val page = slot<Int>()
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = capture(page),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } answers {
            val pageNumber = page.captured
            if (pageNumber == 1) {
                (0..<20).map { idx ->
                    SearchResult.Post(PostModel(id = idx.toLong()))
                }
            } else {
                emptyList()
            }
        }
        val specification =
            ExplorePaginationSpecification(
                query = query,
            )
        sut.reset(specification)

        val items = sut.loadNextPage()

        assertEquals(20, items.size)
        assertTrue(sut.canFetchMore)

        coVerify {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = query,
            )
            communityRepository.getResolved(query = query, auth = AUTH_TOKEN)
        }
    }

    @Test
    fun givenResultsAndSearchQueryAndAllResultType_whenLoadNextPage_thenResultIsAsExpected() = runTest {
        val query = "text"
        val page = slot<Int>()
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = capture(page),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } answers {
            val pageNumber = page.captured
            if (pageNumber == 1) {
                (0..<20).map { idx ->
                    SearchResult.Post(PostModel(id = idx.toLong()))
                }
            } else {
                emptyList()
            }
        }
        val specification =
            ExplorePaginationSpecification(
                resultType = SearchResultType.All,
                query = query,
            )
        sut.reset(specification)

        val items = sut.loadNextPage()

        assertEquals(20, items.size)
        assertTrue(sut.canFetchMore)

        coVerify {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = query,
            )
            commentRepository.getResolved(query = query, auth = AUTH_TOKEN)
            commentRepository.getResolved(query = query, auth = AUTH_TOKEN)
            postRepository.getResolved(query = query, auth = AUTH_TOKEN)
            userRepository.getResolved(query = query, auth = AUTH_TOKEN)
        }
    }

    @Test
    fun givenResultsAndSearchQueryAndSearchPostTitleOnly_whenLoadNextPage_thenResultIsAsExpected() = runTest {
        val query = "text 10"
        val page = slot<Int>()
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = capture(page),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } answers {
            val pageNumber = page.captured
            if (pageNumber == 1) {
                (0..<20).map { idx ->
                    SearchResult.Post(PostModel(id = idx.toLong(), title = "text $idx"))
                }
            } else {
                emptyList()
            }
        }
        val specification =
            ExplorePaginationSpecification(
                resultType = SearchResultType.Posts,
                searchPostTitleOnly = true,
                query = query,
            )
        sut.reset(specification)

        val items = sut.loadNextPage()

        assertEquals(1, items.size)
        assertTrue(sut.canFetchMore)

        coVerify {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = query,
            )
            postRepository.getResolved(query = query, auth = AUTH_TOKEN)
        }
    }

    @Test
    fun givenResultsAndRestrictLocalUserSearch_whenLoadNextPage_thenResultIsAsExpected() = runTest {
        val page = slot<Int>()
        coEvery {
            communityRepository.search(
                query = any(),
                auth = any(),
                page = capture(page),
                limit = any(),
                sortType = any(),
                listingType = any(),
                resultType = any(),
                instance = any(),
                communityId = any(),
            )
        } answers {
            val pageNumber = page.captured
            if (pageNumber == 1) {
                (0..<20).map { idx ->
                    SearchResult.User(
                        UserModel(
                            id = idx.toLong(),
                            host =
                            if (idx == 0) {
                                "instance"
                            } else {
                                ""
                            },
                        ),
                    )
                }
            } else {
                emptyList()
            }
        }
        val specification =
            ExplorePaginationSpecification(
                resultType = SearchResultType.Users,
                listingType = ListingType.Local,
                restrictLocalUserSearch = true,
            )
        sut.reset(specification)

        val items = sut.loadNextPage()

        assertEquals(1, items.size)
        assertTrue(sut.canFetchMore)

        coVerify {
            communityRepository.search(
                auth = AUTH_TOKEN,
                page = 1,
                limit = 20,
                listingType = specification.listingType,
                sortType = specification.sortType,
                communityId = null,
                instance = null,
                resultType = specification.resultType,
                query = "",
            )
        }
    }

    companion object {
        private const val AUTH_TOKEN = "fake-token"
    }
}
