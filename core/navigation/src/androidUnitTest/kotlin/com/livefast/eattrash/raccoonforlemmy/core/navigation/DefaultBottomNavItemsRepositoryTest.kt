package com.livefast.eattrash.raccoonforlemmy.core.navigation

import com.livefast.eattrash.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DefaultBottomNavItemsRepositoryTest {
    @get:Rule
    val dispatcherRule = DispatcherTestRule()

    private val keyStore = mockk<TemporaryKeyStore>(relaxUnitFun = true)
    private val sut =
        DefaultBottomNavItemsRepository(
            keyStore = keyStore,
        )

    @Test
    fun givenNoData_whenGetForAnonymousUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            every { keyStore.get(any(), any<List<String>>()) } returns emptyList()

            val res = sut.get(null)

            assertEquals(BottomNavItemsRepository.DEFAULT_ITEMS, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.items", emptyList())
            }
        }

    @Test
    fun givenData_whenGetForAnonymousUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            every { keyStore.get(any(), any<List<String>>()) } returns ITEMS_IDS

            val res = sut.get(null)

            assertEquals(ITEMS, res)
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

            assertEquals(BottomNavItemsRepository.DEFAULT_ITEMS, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenData_whenGetForLoggedUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val accountId = 1L
            every { keyStore.get(any(), any<List<String>>()) } returns ITEMS_IDS

            val res = sut.get(accountId)

            assertEquals(ITEMS, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenDataForOtherUser_whenGetForLoggedAccount_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val otherAccountId = 1L
            val accountId = 2L
            every {
                keyStore.get(
                    "$KEY_PREFIX.$otherAccountId.items",
                    any<List<String>>(),
                )
            } returns ITEMS_IDS
            every {
                keyStore.get(
                    "$KEY_PREFIX.$accountId.items",
                    any<List<String>>(),
                )
            } returns emptyList()

            val res = sut.get(accountId)

            assertEquals(BottomNavItemsRepository.DEFAULT_ITEMS, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenDataForOtherUser_whenGetForAnonymousAccount_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val otherAccountId = 1
            every {
                keyStore.get(
                    "$KEY_PREFIX.$otherAccountId.items",
                    any<List<String>>(),
                )
            } returns ITEMS_IDS
            every {
                keyStore.get(
                    "$KEY_PREFIX.items",
                    any<List<String>>(),
                )
            } returns emptyList()

            val res = sut.get(null)

            assertEquals(BottomNavItemsRepository.DEFAULT_ITEMS, res)
            coVerify {
                keyStore.get("$KEY_PREFIX.items", emptyList())
            }
        }

    @Test
    fun whenUpdateAnonymousUser_thenInteractionsAreAsExpected() =
        runTest {
            sut.update(accountId = null, items = ITEMS)

            coVerify {
                keyStore.save("$KEY_PREFIX.items", ITEMS_IDS)
            }
        }

    @Test
    fun whenUpdateLoggedUser_thenInteractionsAreAsExpected() =
        runTest {
            val accountId = 1L

            sut.update(accountId = accountId, items = ITEMS)

            coVerify {
                keyStore.save("$KEY_PREFIX.$accountId.items", ITEMS_IDS)
            }
        }

    companion object {
        private val ITEMS_IDS = listOf("0", "1", "3", "2", "4")
        private val ITEMS =
            listOf(
                TabNavigationSection.Home,
                TabNavigationSection.Explore,
                TabNavigationSection.Profile,
                TabNavigationSection.Inbox,
                TabNavigationSection.Settings,
            )
        private const val KEY_PREFIX = "BottomNavItemsRepository"
    }
}
