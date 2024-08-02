package com.eattrash.raccoonforlemmy.domain.lemmy.pagination

import com.eattrash.raccoonforlemmy.domain.lemmy.pagination.ExplorePaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.DomainBlocklistRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.StopWordRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DefaultExplorePaginationManagerTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val identityRepository: IdentityRepository =
        mockk {
            every { authToken } returns MutableStateFlow(AUTH_TOKEN)
            every { cachedUser } returns UserModel(id = 1)
        }
    private val communityRepository: CommunityRepository = mockk(relaxUnitFun = true)
    private val userRepository: UserRepository = mockk(relaxUnitFun = true)
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

    private val sut =
        DefaultExplorePaginationManager(
            identityRepository = identityRepository,
            accountRepository = accountRepository,
            communityRepository = communityRepository,
            userRepository = userRepository,
            domainBlocklistRepository = domainBlocklistRepository,
            stopWordRepository = stopWordRepository,
        )

    @Test
    fun whenReset_thenCanFetchMore() =
        runTest {
            val specification = ExplorePaginationSpecification()
            sut.reset(specification)

            assertTrue(sut.canFetchMore)
        }

    @Test
    fun givenNoResults_whenLoadNextPage_thenResultIsAsExpected() =
        runTest {
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
    fun givenResults_whenLoadNextPage_thenResultIsAsExpected() =
        runTest {
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
    fun givenResults_whenSecondLoadNextPage_thenResultIsAsExpected() =
        runTest {
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

    companion object {
        private const val AUTH_TOKEN = "fake-token"
    }
}
