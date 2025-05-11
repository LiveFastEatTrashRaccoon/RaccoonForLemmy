package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PostServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.UserServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MediaModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultMediaRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val userServiceV3 = mockk<UserServiceV3>()
    private val postServiceV3 = mockk<PostServiceV3>(relaxUnitFun = true)
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { v3 } returns
                mockk {
                    every { user } returns userServiceV3
                    every { post } returns postServiceV3
                }
            every { currentInstance } returns INSTANCE
        }
    private val sut =
        DefaultMediaRepository(
            services = serviceProvider,
        )

    @Test
    fun whenUploadImage_thenInteractionsAreAsExpected() =
        runTest {
            coEvery {
                postServiceV3.uploadImage(
                    authHeader = any(),
                    url = any(),
                    token = any(),
                    content = any(),
                )
            } returns mockk(relaxed = true)

            val res =
                sut.uploadImage(
                    auth = AUTH_TOKEN,
                    bytes = byteArrayOf(),
                )

            assertNotNull(res)
            coVerify {
                postServiceV3.uploadImage(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    url = "https://$INSTANCE/pictrs/image",
                    token = "jwt=${AUTH_TOKEN}",
                    content = any(),
                )
            }
        }

    @Test
    fun givenError_whenUploadImage_thenInteractionsAreAsExpected() =
        runTest {
            coEvery {
                postServiceV3.uploadImage(
                    authHeader = any(),
                    url = any(),
                    token = any(),
                    content = any(),
                )
            } throws IOException("Network error")

            val res =
                sut.uploadImage(
                    auth = AUTH_TOKEN,
                    bytes = byteArrayOf(),
                )

            assertNull(res)
            coVerify {
                postServiceV3.uploadImage(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    url = "https://$INSTANCE/pictrs/image",
                    token = "jwt=${AUTH_TOKEN}",
                    content = any(),
                )
            }
        }

    @Test
    fun givenNoResults_whenGetAll_thenResultAndInteractionsAreAsExpected() =
        runTest {
            coEvery {
                userServiceV3.listMedia(
                    authHeader = any(),
                    page = any(),
                    limit = any(),
                )
            } returns
                mockk {
                    every { images } returns emptyList()
                }

            val res =
                sut.getAll(
                    auth = AUTH_TOKEN,
                    page = 1,
                )

            assertTrue(res.isEmpty())
            coVerify {
                userServiceV3.listMedia(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    page = 1,
                    limit = 20,
                )
            }
        }

    @Test
    fun givenError_whenGetAll_thenResultAndInteractionsAreAsExpected() =
        runTest {
            coEvery {
                userServiceV3.listMedia(
                    authHeader = any(),
                    page = any(),
                    limit = any(),
                )
            } throws IOException("Network error")

            val res =
                sut.getAll(
                    auth = AUTH_TOKEN,
                    page = 1,
                )

            assertTrue(res.isEmpty())
            coVerify {
                userServiceV3.listMedia(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    page = 1,
                    limit = 20,
                )
            }
        }

    @Test
    fun givenResults_whenGetAll_thenResultAndInteractionsAreAsExpected() =
        runTest {
            coEvery {
                userServiceV3.listMedia(
                    authHeader = any(),
                    page = any(),
                    limit = any(),
                )
            } returns
                mockk {
                    every { images } returns
                        listOf(
                            mockk(relaxed = true) {
                                every { localImage } returns
                                    mockk(relaxed = true) { every { pictrsAlias } returns "fake-alias" }
                            },
                        )
                }

            val res =
                sut.getAll(
                    auth = AUTH_TOKEN,
                    page = 1,
                )

            assertEquals(1, res.size)
            assertEquals("fake-alias", res.first().alias)
            coVerify {
                userServiceV3.listMedia(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    page = 1,
                    limit = 20,
                )
            }
        }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() =
        runTest {
            val instance = "fake-instance"

            val media = MediaModel(alias = "fake-alias", deleteToken = "fake-delete-token")
            sut.delete(
                auth = AUTH_TOKEN,
                media = media,
            )

            coVerify {
                postServiceV3.deleteImage(
                    authHeader = AUTH_TOKEN.toAuthHeader(),
                    url = "https://$instance/pictrs/image/delete/fake-delete-token/fake-alias",
                    token = "jwt=${AUTH_TOKEN}",
                )
            }
        }

    companion object {
        private const val AUTH_TOKEN = "fake-auth-token"
        private const val INSTANCE = "fake-instance"
    }
}
