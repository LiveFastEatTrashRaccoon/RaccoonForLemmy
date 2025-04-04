package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchType
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.CommunityService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.SearchService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.SiteService
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultCommunityRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val communityService = mockk<CommunityService>()
    private val searchService = mockk<SearchService>()
    private val siteService = mockk<SiteService>()
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { community } returns communityService
            every { search } returns searchService
            every { site } returns siteService
        }
    private val customServiceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { community } returns communityService
            every { search } returns searchService
            every { site } returns siteService
        }
    private val sut =
        DefaultCommunityRepository(
            services = serviceProvider,
            customServices = customServiceProvider,
        )

    @Test
    fun givenSuccess_whenSearch_thenResultIsAsExpected() =
        runTest {
            coEvery {
                searchService.search(
                    authHeader = any(),
                    auth = any(),
                    q = any(),
                    communityId = any(),
                    communityName = any(),
                    creatorId = any(),
                    type = any(),
                    sort = any(),
                    listingType = any(),
                    page = any(),
                    limit = any(),
                )
            } returns
                SearchResponse(
                    type = SearchType.Communities,
                    comments = emptyList(),
                    posts = emptyList(),
                    communities =
                        listOf(
                            mockk(relaxed = true),
                        ),
                    users = emptyList(),
                )

            val token = AUTH_TOKEN
            val query = "q"
            val res =
                sut.search(
                    query = query,
                    auth = token,
                    page = 1,
                    limit = 20,
                    listingType = ListingType.All,
                    sortType = SortType.Active,
                    resultType = SearchResultType.Communities,
                )

            assertEquals(1, res.size)
            assertIs<SearchResult.Community>(res.first())
            coVerify {
                customServiceProvider wasNot Called
                searchService.search(
                    authHeader = token.toAuthHeader(),
                    auth = token,
                    q = query,
                    communityId = null,
                    communityName = null,
                    creatorId = null,
                    type = SearchType.Communities,
                    sort = SortType.Active.toDto(),
                    listingType = ListingType.All.toDto(),
                    page = 1,
                    limit = 20,
                )
            }
        }

    @Test
    fun givenSuccess_whenGetList_thenResultIsAsExpected() =
        runTest {
            coEvery {
                communityService.getAll(
                    authHeader = any(),
                    auth = any(),
                    sort = any(),
                    page = any(),
                    limit = any(),
                )
            } returns
                mockk {
                    every { communities } returns
                        listOf(
                            mockk(relaxed = true),
                        )
                }

            val otherInstance = "other-instance"
            val res =
                sut.getList(
                    page = 1,
                    instance = otherInstance,
                    limit = 20,
                    sortType = SortType.Active,
                )

            assertEquals(1, res.size)
            coVerify {
                serviceProvider wasNot Called
                customServiceProvider.changeInstance(otherInstance)
                customServiceProvider.community
                communityService.getAll(
                    authHeader = null,
                    auth = null,
                    page = 1,
                    showNsfw = any(),
                    limit = 20,
                    sort = SortType.Active.toDto(),
                )
            }
        }

    @Test
    fun givenSuccess_whenGetResolved_thenResultIsAsExpected() =
        runTest {
            coEvery {
                searchService.resolveObject(
                    authHeader = any(),
                    q = any(),
                )
            } returns
                mockk {
                    every { community } returns mockk(relaxed = true)
                }

            val token = AUTH_TOKEN
            val query = "q"
            val res =
                sut.getResolved(
                    query = query,
                    auth = token,
                )

            assertNotNull(res)
            coVerify {
                searchService.resolveObject(
                    authHeader = token.toAuthHeader(),
                    q = query,
                )
            }
        }

    @Test
    fun givenSuccess_whenGetSubscribed_thenResultIsAsExpected() =
        runTest {
            coEvery {
                searchService.search(
                    authHeader = any(),
                    auth = any(),
                    q = any(),
                    communityId = any(),
                    communityName = any(),
                    creatorId = any(),
                    type = any(),
                    sort = any(),
                    listingType = any(),
                    page = any(),
                    limit = any(),
                )
            } returns
                mockk {
                    every { communities } returns
                        listOf(
                            mockk(relaxed = true) {
                                every { community } returns mockk(relaxed = true)
                            },
                        )
                }

            val token = AUTH_TOKEN
            val res =
                sut.getSubscribed(
                    auth = token,
                    page = 1,
                )

            assertEquals(1, res.size)
            coVerify {
                searchService.search(
                    authHeader = token.toAuthHeader(),
                    auth = token,
                    q = "",
                    communityId = null,
                    communityName = null,
                    creatorId = null,
                    type = SearchType.Communities,
                    sort = null,
                    listingType = ListingType.Subscribed.toDto(),
                    page = 1,
                    limit = 20,
                )
            }
        }

    @Test
    fun givenSuccess_whenGet_thenResultIsAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.get(
                    authHeader = any(),
                    auth = any(),
                    id = any(),
                    name = any(),
                )
            } returns
                mockk {
                    every { communityView } returns
                        mockk(relaxed = true) {
                            every { community } returns
                                mockk(relaxed = true) {
                                    every { id } returns communityId
                                }
                        }
                }

            val token = AUTH_TOKEN
            val res =
                sut.get(
                    auth = token,
                    id = communityId,
                )

            assertNotNull(res)
            assertEquals(communityId, res.id)
            coVerify {
                communityService.get(
                    id = communityId,
                    name = null,
                    auth = token,
                    authHeader = token.toAuthHeader(),
                )
            }
        }

    @Test
    fun givenSuccess_whenGetModerators_thenResultIsAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.get(
                    authHeader = any(),
                    auth = any(),
                    id = any(),
                    name = any(),
                )
            } returns
                mockk {
                    every { communityView } returns
                        mockk(relaxed = true) {
                            every { community } returns
                                mockk(relaxed = true) {
                                    every { id } returns communityId
                                    every { moderators } returns listOf(mockk(relaxed = true))
                                }
                        }
                }

            val token = AUTH_TOKEN
            val res =
                sut.getModerators(
                    auth = token,
                    id = communityId,
                )

            assertEquals(1, res.size)
            coVerify {
                communityService.get(
                    id = communityId,
                    name = null,
                    auth = token,
                    authHeader = token.toAuthHeader(),
                )
            }
        }

    @Test
    fun givenSuccess_whenSubscribe_thenResultIsAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.follow(any(), any())
            } returns
                mockk {
                    every { communityView } returns
                        mockk(relaxed = true) {
                            every { community } returns
                                mockk(relaxed = true) {
                                    every { id } returns communityId
                                }
                        }
                }

            val token = AUTH_TOKEN
            val res =
                sut.subscribe(
                    auth = token,
                    id = communityId,
                )

            assertNotNull(res)
            assertEquals(communityId, res.id)
            coVerify {
                communityService.follow(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(communityId, data.communityId)
                        assertTrue(data.follow)
                    },
                )
            }
        }

    @Test
    fun givenSuccess_whenUnsubscribe_thenResultIsAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.follow(any(), any())
            } returns
                mockk {
                    every { communityView } returns
                        mockk(relaxed = true) {
                            every { community } returns
                                mockk(relaxed = true) {
                                    every { id } returns communityId
                                }
                        }
                }

            val token = AUTH_TOKEN
            val res =
                sut.unsubscribe(
                    auth = token,
                    id = communityId,
                )

            assertNotNull(res)
            assertEquals(communityId, res.id)
            coVerify {
                communityService.follow(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(communityId, data.communityId)
                        assertFalse(data.follow)
                    },
                )
            }
        }

    @Test
    fun givenSuccess_whenBlock_thenResultIsAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.block(any(), any())
            } returns mockk()
            val token = AUTH_TOKEN

            sut.block(
                auth = token,
                id = communityId,
                blocked = true,
            )

            coVerify {
                communityService.block(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(communityId, data.communityId)
                        assertTrue(data.block)
                    },
                )
            }
        }

    @Test
    fun givenSuccess_whenBanUser_thenInteractionsAreAsExpected() =
        runTest {
            val communityId = 1L
            val userId = 2L
            coEvery {
                communityService.ban(any(), any())
            } returns mockk()

            val token = AUTH_TOKEN
            sut.banUser(
                auth = token,
                communityId = communityId,
                userId = userId,
                ban = true,
            )

            coVerify {
                communityService.ban(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(communityId, data.communityId)
                        assertEquals(userId, data.personId)
                        assertTrue(data.ban)
                    },
                )
            }
        }

    @Test
    fun givenSuccess_whenAddModerator_thenInteractionsAreAsExpected() =
        runTest {
            val communityId = 1L
            val userId = 2L
            coEvery {
                communityService.addMod(any(), any())
            } returns mockk()

            val token = AUTH_TOKEN
            sut.addModerator(
                auth = token,
                communityId = communityId,
                userId = userId,
                added = true,
            )

            coVerify {
                communityService.addMod(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(communityId, data.communityId)
                        assertEquals(userId, data.personId)
                        assertTrue(data.added)
                    },
                )
            }
        }

    @Test
    fun givenSuccess_whenUpdate_thenInteractionsAreAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.edit(any(), any())
            } returns mockk()

            val token = AUTH_TOKEN
            val newName = "fake-community-name"
            val data = CommunityModel(id = communityId, title = newName)
            sut.update(
                auth = token,
                community = data,
            )

            coVerify {
                communityService.edit(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(communityId, data.communityId)
                        assertEquals(newName, data.title)
                    },
                )
            }
        }

    @Test
    fun givenSuccess_whenCreate_thenInteractionsAreAsExpected() =
        runTest {
            coEvery {
                communityService.create(any(), any())
            } returns mockk(relaxed = true)

            val token = AUTH_TOKEN
            val newName = "fake-community-name"
            val data = CommunityModel(name = newName)

            val res =
                sut.create(
                    auth = token,
                    community = data,
                )

            assertNotNull(res)
            coVerify {
                communityService.create(
                    authHeader = token.toAuthHeader(),
                    withArg { data ->
                        assertEquals(newName, data.name)
                    },
                )
            }
        }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.delete(any(), any())
            } returns mockk()

            sut.delete(
                auth = AUTH_TOKEN,
                communityId = communityId,
            )

            coVerify {
                communityService.delete(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    withArg {
                        assertEquals(1, it.communityId)
                        assertTrue(it.deleted)
                    },
                )
            }
        }

    @Test
    fun whenHide_thenInteractionsAreAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.hide(any(), any())
            } returns mockk { every { success } returns true }

            sut.hide(
                auth = AUTH_TOKEN,
                communityId = communityId,
                hidden = true,
                reason = "fake-reason",
            )

            coVerify {
                communityService.hide(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    withArg {
                        assertEquals(1, it.communityId)
                        assertTrue(it.hidden)
                        assertEquals("fake-reason", it.reason)
                    },
                )
            }
        }

    @Test
    fun whenPurge_thenInteractionsAreAsExpected() =
        runTest {
            val communityId = 1L
            coEvery {
                communityService.purge(any(), any())
            } returns mockk { every { success } returns true }

            sut.purge(
                auth = AUTH_TOKEN,
                communityId = communityId,
                reason = "fake-reason",
            )

            coVerify {
                communityService.purge(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    withArg {
                        assertEquals(1, it.communityId)
                        assertEquals("fake-reason", it.reason)
                    },
                )
            }
        }

    companion object {
        private const val AUTH_TOKEN = "fake-token"
    }
}
