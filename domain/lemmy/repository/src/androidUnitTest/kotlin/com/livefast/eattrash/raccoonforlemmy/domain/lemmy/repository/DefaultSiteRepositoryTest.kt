package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.Language
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SiteMetadata
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PostServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SiteServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.UserServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.AccountServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.AccountSettingsModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultSiteRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val siteServiceV3 = mockk<SiteServiceV3>()
    private val postServiceV3 = mockk<PostServiceV3>()
    private val userServiceV3 = mockk<UserServiceV3>()
    private val accountServiceV4 = mockk<AccountServiceV4>()
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { v3 } returns
                mockk {
                    every { site } returns siteServiceV3
                    every { post } returns postServiceV3
                    every { user } returns userServiceV3
                }
            every { v4 } returns
                mockk {
                    every { account } returns accountServiceV4
                }
            every { currentInstance } returns "feddit.it"
        }
    private val customServiceProvider =
        mockk<ServiceProvider> {
            every { v3 } returns
                mockk {
                    every { site } returns siteServiceV3
                    every { post } returns postServiceV3
                }
            every { currentInstance } returns "lemmy.ml"
        }
    private val siteVersionDataSource =
        mockk<SiteVersionDataSource> {
            coEvery {
                isAtLeast(
                    major = any(),
                    minor = any(),
                    patch = any(),
                    otherInstance = any(),
                )
            } returns false
        }

    private val sut =
        DefaultSiteRepository(
            services = serviceProvider,
            customServices = customServiceProvider,
            siteVersionDataSource = siteVersionDataSource,
        )

    @Test
    fun givenV3_whenGetCurrentUser_thenResultAndInteractionsAreAsExpected() = runTest {
        val userId = 1L
        coEvery {
            siteServiceV3.get(auth = any(), authHeader = any())
        } returns
            mockk(relaxed = true) {
                every { myUser } returns
                    mockk(relaxed = true) {
                        every { localUserView } returns
                            mockk(relaxed = true) {
                                every { person } returns
                                    mockk(relaxed = true) {
                                        every { id } returns userId
                                    }
                            }
                    }
            }

        val res = sut.getCurrentUser(auth = AUTH_TOKEN)

        assertNotNull(res)
        assertEquals(userId, res.id)
        coVerify {
            siteServiceV3.get(
                auth = AUTH_TOKEN,
                authHeader = AUTH_TOKEN.toAuthHeader(),
            )
            accountServiceV4 wasNot Called
        }
    }

    @Test
    fun givenV4_whenGetCurrentUser_thenResultAndInteractionsAreAsExpected() = runTest {
        coEvery {
            siteVersionDataSource.isAtLeast(
                major = any(),
                minor = any(),
                patch = any(),
                otherInstance = any(),
            )
        } returns true
        val userId = 1L
        coEvery {
            accountServiceV4.get(authHeader = any())
        } returns
            mockk(relaxed = true) {
                mockk(relaxed = true) {
                    every { localUserView } returns
                        mockk(relaxed = true) {
                            every { person } returns
                                mockk(relaxed = true) {
                                    every { id } returns userId
                                }
                        }
                }
            }

        val res = sut.getCurrentUser(auth = AUTH_TOKEN)

        assertNotNull(res)
        assertEquals(userId, res.id)
        coVerify {
            siteServiceV3 wasNot Called
            accountServiceV4.get(authHeader = AUTH_TOKEN.toAuthHeader())
        }
    }

    @Test
    fun whenGetSiteVersion_thenResultAndInteractionsAreAsExpected() = runTest {
        val versionValue = "0.19.1"
        coEvery {
            siteServiceV3.get(auth = any(), authHeader = any())
        } returns
            mockk {
                every { version } returns versionValue
            }

        val res = sut.getSiteVersion()

        assertEquals(versionValue, res)
        coVerify {
            siteServiceV3.get()
        }
    }

    @Test
    fun whenBlock_thenResultAndInteractionsAreAsExpected() = runTest {
        val instanceId = 1L
        val formSlot = slot<BlockInstanceForm>()
        coEvery {
            siteServiceV3.block(authHeader = any(), form = capture(formSlot))
        } answers {
            BlockInstanceResponse(blocked = formSlot.captured.block)
        }

        sut.block(
            auth = AUTH_TOKEN,
            id = instanceId,
            blocked = true,
        )

        coVerify {
            siteServiceV3.block(
                authHeader = AUTH_TOKEN.toAuthHeader(),
                form =
                withArg {
                    assertTrue(it.block)
                    assertEquals(instanceId, it.instanceId)
                },
            )
        }
    }

    @Test
    fun whenGetMetadata_thenResultAndInteractionsAreAsExpected() = runTest {
        val instanceTitle = "feddit.it"
        val siteMetadata = SiteMetadata(title = instanceTitle)
        coEvery {
            postServiceV3.getSiteMetadata(any(), any())
        } returns
            mockk {
                every { metadata } returns siteMetadata
            }

        val res = sut.getMetadata("https://$instanceTitle")

        assertNotNull(res)
        assertEquals(instanceTitle, res.title)
        coVerify {
            postServiceV3.getSiteMetadata(
                url = "https://$instanceTitle",
            )
        }
    }

    @Test
    fun whenGetLanguages_thenResultAndInteractionsAreAsExpected() = runTest {
        val languages = listOf(Language(id = 0, code = "en", name = "English"))
        coEvery {
            siteServiceV3.get(auth = any(), authHeader = any())
        } returns
            mockk {
                every { allLanguages } returns languages
            }

        val res = sut.getLanguages(auth = AUTH_TOKEN)

        assertEquals(1, res.size)
        assertEquals(0L, res.first().id)
        assertEquals("en", res.first().code)
        coVerify {
            siteServiceV3.get(
                auth = AUTH_TOKEN,
                authHeader = AUTH_TOKEN.toAuthHeader(),
            )
        }
    }

    @Test
    fun whenGetAccountSettings_thenResultAndInteractionsAreAsExpected() = runTest {
        val personBio = "fake-bio"
        coEvery {
            siteServiceV3.get(auth = any(), authHeader = any())
        } returns
            mockk(relaxed = true) {
                every { myUser } returns
                    mockk(relaxed = true) {
                        every { localUserView } returns
                            mockk(relaxed = true) {
                                every { localUser } returns
                                    mockk(relaxed = true) {
                                        every { person } returns
                                            mockk(relaxed = true) {
                                                every { bio } returns personBio
                                            }
                                    }
                            }
                    }
            }

        val res = sut.getAccountSettings(auth = AUTH_TOKEN)

        assertNotNull(res)
        assertEquals(personBio, res.bio)
        coVerify {
            siteServiceV3.get(
                auth = AUTH_TOKEN,
                authHeader = AUTH_TOKEN.toAuthHeader(),
            )
        }
    }

    @Test
    fun whenUpdateAccountSettings_thenResultAndInteractionsAreAsExpected() = runTest {
        val personBio = "fake-bio"
        coEvery {
            userServiceV3.saveUserSettings(authHeader = any(), form = any())
        } returns mockk(relaxed = true)

        sut.updateAccountSettings(
            auth = AUTH_TOKEN,
            value = AccountSettingsModel(bio = personBio),
        )

        coVerify {
            userServiceV3.saveUserSettings(
                authHeader = AUTH_TOKEN.toAuthHeader(),
                form =
                withArg {
                    assertEquals(personBio, it.bio)
                },
            )
        }
    }

    @Test
    fun whenGetBans_thenResultAndInteractionsAreAsExpected() = runTest {
        coEvery {
            siteServiceV3.get(auth = any(), authHeader = any())
        } returns
            mockk(relaxed = true) {
                every { myUser } returns
                    mockk(relaxed = true) {
                        every { personBlocks } returns
                            listOf(
                                mockk(relaxed = true) {
                                    every { target } returns mockk(relaxed = true) { every { id } returns 1L }
                                },
                            )
                        every { communityBlocks } returns
                            listOf(
                                mockk(relaxed = true) {
                                    every { community } returns mockk(relaxed = true) { every { id } returns 2L }
                                },
                            )
                        every { instanceBlocks } returns
                            listOf(
                                mockk(relaxed = true) {
                                    every { instance } returns mockk(relaxed = true) { every { id } returns 3L }
                                },
                            )
                    }
            }
        val res = sut.getBans(auth = AUTH_TOKEN)

        assertNotNull(res)
        assertTrue(res.users.isNotEmpty())
        assertEquals(1L, res.users.first().id)
        assertTrue(res.communities.isNotEmpty())
        assertEquals(2L, res.communities.first().id)
        assertTrue(res.instances.isNotEmpty())
        assertEquals(3L, res.instances.first().id)
        coVerify {
            siteServiceV3.get(
                auth = AUTH_TOKEN,
                authHeader = AUTH_TOKEN.toAuthHeader(),
            )
        }
    }

    @Test
    fun whenGetAdmins_thenResultAndInteractionsAreAsExpected() = runTest {
        coEvery {
            siteServiceV3.get(auth = any(), authHeader = any())
        } returns
            mockk {
                every { admins } returns
                    listOf(
                        mockk(relaxed = true) {
                            every { person } returns
                                mockk(relaxed = true) {
                                    every { id } returns 1L
                                }
                        },
                    )
            }

        val res = sut.getAdmins()

        assertTrue(res.isNotEmpty())
        assertEquals(1L, res.first().id)
    }

    companion object {
        private const val AUTH_TOKEN = "fake-auth-token"
    }
}
