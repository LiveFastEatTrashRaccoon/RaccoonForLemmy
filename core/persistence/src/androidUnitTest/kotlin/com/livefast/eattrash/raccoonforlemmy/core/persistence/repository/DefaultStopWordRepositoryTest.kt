package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DefaultStopWordRepositoryTest {
    @get:Rule
    val dispatcherRule = DispatcherTestRule()

    private val keyStore = mockk<TemporaryKeyStore>(relaxUnitFun = true)
    private val sut =
        DefaultStopWordRepository(
            keyStore = keyStore,
        )

    @Test
    fun givenNoData_whenGetForAnonymousUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            every { keyStore.get(any(), any<List<String>>()) } returns emptyList()

            val res = sut.get(null)

            assertEquals(emptyList(), res)
            coVerify {
                keyStore.get("$KEY_PREFIX.items", emptyList())
            }
        }

    @Test
    fun givenData_whenGetForAnonymousUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val fakeList = listOf("word")
            every { keyStore.get(any(), any<List<String>>()) } returns fakeList

            val res = sut.get(null)

            assertEquals(fakeList, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.items", emptyList())
            }
        }

    @Test
    fun givenNoData_whenGetForLoggedUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val accountId = 1L
            every { keyStore.get(any(), any<List<String>>()) } returns emptyList()

            val res = sut.get(accountId)

            assertEquals(emptyList(), res)
            coVerify {
                keyStore.get("$KEY_PREFIX.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenData_whenGetForLoggedUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val accountId = 1L
            val fakeList = listOf("word")
            every { keyStore.get(any(), any<List<String>>()) } returns fakeList

            val res = sut.get(accountId)

            assertEquals(fakeList, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenDataForOtherUser_whenGetForLoggedAccount_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val otherAccountId = 1L
            val accountId = 2L
            val fakeList = listOf("word")
            every {
                keyStore.get(
                    "$KEY_PREFIX.$otherAccountId.items",
                    any<List<String>>(),
                )
            } returns fakeList
            every {
                keyStore.get(
                    "$KEY_PREFIX.$accountId.items",
                    any<List<String>>(),
                )
            } returns emptyList()

            val res = sut.get(accountId)

            assertEquals(emptyList(), res)
            coVerify {
                keyStore.get("$KEY_PREFIX.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenDataForOtherUser_whenGetForAnonymousAccount_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val otherAccountId = 1
            val fakeList = listOf("word")
            every {
                keyStore.get(
                    "$KEY_PREFIX.$otherAccountId.items",
                    any<List<String>>(),
                )
            } returns fakeList
            every {
                keyStore.get(
                    "$KEY_PREFIX.items",
                    any<List<String>>(),
                )
            } returns emptyList()

            val res = sut.get(null)

            assertEquals(emptyList(), res)
            coVerify {
                keyStore.get("$KEY_PREFIX.items", emptyList())
            }
        }

    @Test
    fun whenUpdateAnonymousUser_thenInteractionsAreAsExpected() =
        runTest {
            val fakeList = listOf("word")
            sut.update(accountId = null, items = fakeList)

            coVerify {
                keyStore.save("$KEY_PREFIX.items", fakeList)
            }
        }

    @Test
    fun whenUpdateLoggedUser_thenInteractionsAreAsExpected() =
        runTest {
            val accountId = 1L
            val fakeList = listOf("word")

            sut.update(accountId = accountId, items = fakeList)

            coVerify {
                keyStore.save("$KEY_PREFIX.$accountId.items", fakeList)
            }
        }

    companion object {
        private const val KEY_PREFIX = "StopWordRepository"
    }
}
