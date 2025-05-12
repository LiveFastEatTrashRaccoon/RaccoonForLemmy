package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SearchType
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.CommunityServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SearchServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SiteServiceV3
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

    private val communityServiceV3 = mockk<CommunityServiceV3>()
    private val searchServiceV3 = mockk<SearchServiceV3>()
    private val siteServiceV3 = mockk<SiteServiceV3>()
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { v3 } returns
                mockk {
                    every { community } returns communityServiceV3
                    every { search } returns searchServiceV3
                    every { site } returns siteServiceV3
                }
        }
    private val customServiceProvider =
        mockk<ServiceProvider>(relaxUnitFun = true) {
            every { v3 } returns
                mockk {
                    every { community } returns communityServiceV3
                    every { search } returns searchServiceV3
                    every { site } returns siteServiceV3
                }
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
                searchServiceV3.search(
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
                searchServiceV3.search(
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
                communityServiceV3.getAll(
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
                communityServiceV3.getAll(
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
                searchServiceV3.resolveObject(
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
                searchServiceV3.resolveObject(
                    authHeader = token.toAuthHeader(),
                    q = query,
                )
            }
        }

    @Test
    fun givenSuccess_whenGetSubscribed_thenResultIsAsExpected() =
        runTest {
            coEvery {
                searchServiceV3.search(
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
                searchServiceV3.search(
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
                communityServiceV3.get(
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
                communityServiceV3.get(
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
                communityServiceV3.get(
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
                communityServiceV3.get(
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
                communityServiceV3.follow(any(), any())
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
                communityServiceV3.follow(
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
                communityServiceV3.follow(any(), any())
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
                communityServiceV3.follow(
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
                communityServiceV3.block(any(), any())
            } returns mockk()
            val token = AUTH_TOKEN

            sut.block(
                auth = token,
                id = communityId,
                blocked = true,
            )

            coVerify {
                communityServiceV3.block(
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
                communityServiceV3.ban(any(), any())
            } returns mockk()

            val token = AUTH_TOKEN
            sut.banUser(
                auth = token,
                communityId = communityId,
                userId = userId,
                ban = true,
            )

            coVerify {
                communityServiceV3.ban(
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
                communityServiceV3.addMod(any(), any())
            } returns mockk()

            val token = AUTH_TOKEN
            sut.addModerator(
                auth = token,
                communityId = communityId,
                userId = userId,
                added = true,
            )

            coVerify {
                communityServiceV3.addMod(
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
                communityServiceV3.edit(any(), any())
            } returns mockk()

            val token = AUTH_TOKEN
            val newName = "fake-community-name"
            val data = CommunityModel(id = communityId, title = newName)
            sut.update(
                auth = token,
                community = data,
            )

            coVerify {
                communityServiceV3.edit(
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
                communityServiceV3.create(any(), any())
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
                communityServiceV3.create(
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
                communityServiceV3.delete(any(), any())
            } returns mockk()

            sut.delete(
                auth = AUTH_TOKEN,
                communityId = communityId,
            )

            coVerify {
                communityServiceV3.delete(
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
                communityServiceV3.hide(any(), any())
            } returns mockk { every { success } returns true }

            sut.hide(
                auth = AUTH_TOKEN,
                communityId = communityId,
                hidden = true,
                reason = "fake-reason",
            )

            coVerify {
                communityServiceV3.hide(
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
                communityServiceV3.purge(any(), any())
            } returns mockk { every { success } returns true }

            sut.purge(
                auth = AUTH_TOKEN,
                communityId = communityId,
                reason = "fake-reason",
            )

            coVerify {
                communityServiceV3.purge(
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
