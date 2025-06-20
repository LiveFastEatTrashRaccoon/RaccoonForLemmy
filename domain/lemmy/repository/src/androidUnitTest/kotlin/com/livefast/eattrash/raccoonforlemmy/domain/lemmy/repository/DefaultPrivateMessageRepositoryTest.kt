package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PrivateMessageServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultPrivateMessageRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val privateMessageServiceV3 = mockk<PrivateMessageServiceV3>()
    private val serviceProvider =
        mockk<ServiceProvider> {
            every { v3 } returns
                mockk {
                    every { privateMessages } returns privateMessageServiceV3
                }
        }

    private val sut =
        DefaultPrivateMessageRepository(
            services = serviceProvider,
        )

    @Test
    fun givenNoResults_whenGetAll_thenResultAndInteractionsAreAsExpected() = runTest {
        val creatorId = 1L
        coEvery {
            privateMessageServiceV3.getAll(
                auth = any(),
                authHeader = any(),
                page = any(),
                creatorId = any(),
                limit = any(),
                unreadOnly = any(),
            )
        } returns
            mockk {
                every { privateMessages } returns emptyList()
            }

        val res =
            sut.getAll(
                auth = AUTH_TOKEN,
                creatorId = creatorId,
                page = 1,
                unreadOnly = true,
            )

        assertNotNull(res)
        assertTrue(res.isEmpty())
        coVerify {
            privateMessageServiceV3.getAll(
                auth = AUTH_TOKEN,
                authHeader = AUTH_TOKEN.toAuthHeader(),
                page = 1,
                creatorId = creatorId,
                limit = 50,
                unreadOnly = true,
            )
        }
    }

    @Test
    fun givenResults_whenGetAll_thenResultAndInteractionsAreAsExpected() = runTest {
        val creatorId = 1L
        val messageId = 2L
        coEvery {
            privateMessageServiceV3.getAll(
                auth = any(),
                authHeader = any(),
                page = any(),
                creatorId = any(),
                limit = any(),
                unreadOnly = any(),
            )
        } returns
            mockk {
                every { privateMessages } returns
                    listOf(
                        mockk(relaxed = true) {
                            every { creator } returns mockk(relaxed = true) { every { id } returns creatorId }
                            every { privateMessage } returns
                                mockk(relaxed = true) { every { id } returns messageId }
                        },
                    )
            }

        val res =
            sut.getAll(
                auth = AUTH_TOKEN,
                creatorId = creatorId,
                page = 1,
                unreadOnly = true,
            )

        assertNotNull(res)
        assertTrue(res.isNotEmpty())
        assertEquals(messageId, res.first().id)
        coVerify {
            privateMessageServiceV3.getAll(
                auth = AUTH_TOKEN,
                authHeader = AUTH_TOKEN.toAuthHeader(),
                page = 1,
                creatorId = creatorId,
                limit = 50,
                unreadOnly = true,
            )
        }
    }

    @Test
    fun whenCreate_thenResultAndInteractionsAreAsExpected() = runTest {
        val message = "fake-message"
        val recipientId = 1L
        coEvery {
            privateMessageServiceV3.create(
                authHeader = any(),
                form = any(),
            )
        } returns
            mockk {
                every { privateMessageView } returns
                    mockk(relaxed = true) {
                        every { privateMessage } returns
                            mockk(relaxed = true) {
                                every { content } returns message
                            }
                    }
            }

        val res =
            sut.create(
                message = message,
                auth = AUTH_TOKEN,
                recipientId = recipientId,
            )

        assertNotNull(res)
        assertEquals(message, res.content)
        coVerify {
            privateMessageServiceV3.create(
                authHeader = AUTH_TOKEN.toAuthHeader(),
                form =
                withArg {
                    assertEquals(message, it.content)
                    assertEquals(recipientId, it.recipientId)
                    assertEquals(AUTH_TOKEN, it.auth)
                },
            )
        }
    }

    @Test
    fun whenEdit_thenResultAndInteractionsAreAsExpected() = runTest {
        val message = "fake-message"
        val messageId = 1L
        coEvery {
            privateMessageServiceV3.edit(
                authHeader = any(),
                form = any(),
            )
        } returns
            mockk {
                every { privateMessageView } returns
                    mockk(relaxed = true) {
                        every { privateMessage } returns
                            mockk(relaxed = true) {
                                every { content } returns message
                            }
                    }
            }

        val res =
            sut.edit(
                message = message,
                auth = AUTH_TOKEN,
                messageId = messageId,
            )

        assertNotNull(res)
        assertEquals(message, res.content)
        coVerify {
            privateMessageServiceV3.edit(
                authHeader = AUTH_TOKEN.toAuthHeader(),
                form =
                withArg {
                    assertEquals(message, it.content)
                    assertEquals(messageId, it.privateMessageId)
                    assertEquals(AUTH_TOKEN, it.auth)
                },
            )
        }
    }

    @Test
    fun whenMarkAsRead_thenResultAndInteractionsAreAsExpected() = runTest {
        val message = "fake-message"
        val messageId = 1L
        coEvery {
            privateMessageServiceV3.markAsRead(
                authHeader = any(),
                form = any(),
            )
        } returns
            mockk {
                every { privateMessageView } returns
                    mockk(relaxed = true) {
                        every { privateMessage } returns
                            mockk(relaxed = true) {
                                every { content } returns message
                            }
                    }
            }

        val res =
            sut.markAsRead(
                read = true,
                auth = AUTH_TOKEN,
                messageId = messageId,
            )

        assertNotNull(res)
        coVerify {
            privateMessageServiceV3.markAsRead(
                authHeader = AUTH_TOKEN.toAuthHeader(),
                form =
                withArg {
                    assertTrue(it.read)
                    assertEquals(messageId, it.privateMessageId)
                    assertEquals(AUTH_TOKEN, it.auth)
                },
            )
        }
    }

    @Test
    fun whenDelete_thenResultAndInteractionsAreAsExpected() = runTest {
        val message = "fake-message"
        val messageId = 1L
        coEvery {
            privateMessageServiceV3.delete(
                authHeader = any(),
                form = any(),
            )
        } returns
            mockk {
                every { privateMessageView } returns
                    mockk(relaxed = true) {
                        every { privateMessage } returns
                            mockk(relaxed = true) {
                                every { content } returns message
                            }
                    }
            }

        val res =
            sut.delete(
                auth = AUTH_TOKEN,
                messageId = messageId,
            )

        assertNotNull(res)
        coVerify {
            privateMessageServiceV3.delete(
                authHeader = AUTH_TOKEN.toAuthHeader(),
                form =
                withArg {
                    assertEquals(messageId, it.privateMessageId)
                    assertEquals(AUTH_TOKEN, it.auth)
                },
            )
        }
    }

    companion object {
        private const val AUTH_TOKEN = "fake-auth-token"
    }
}
