package com.github.diegoberaldin.raccoonforlemmy.core.navigation

import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.core.testutils.DispatcherTestRule
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
                keyStore.get("BottomNavItemsRepository.items", emptyList())
            }
        }

    @Test
    fun givenData_whenGetForAnonymousUser_thenResultAndInteractionsIsAsExpected() =
        runTest {
            every { keyStore.get(any(), any<List<String>>()) } returns ITEMS_IDS

            val res = sut.get(null)

            assertEquals(ITEMS, res)
            coVerify {
                keyStore.get("BottomNavItemsRepository.items", emptyList())
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
                keyStore.get("BottomNavItemsRepository.$accountId.items", emptyList())
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
                keyStore.get("BottomNavItemsRepository.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenDataForOtherUser_whenGetForLoggedAccount_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val otherAccountId = 1L
            val accountId = 2L
            every { keyStore.get("BottomNavItemsRepository.$otherAccountId.items", any<List<String>>()) } returns ITEMS_IDS
            every { keyStore.get("BottomNavItemsRepository.$accountId.items", any<List<String>>()) } returns emptyList()

            val res = sut.get(accountId)

            assertEquals(BottomNavItemsRepository.DEFAULT_ITEMS, res)
            coVerify {
                keyStore.get("BottomNavItemsRepository.$accountId.items", emptyList())
            }
        }

    @Test
    fun givenDataForOtherUser_whenGetForAnonymousAccount_thenResultAndInteractionsIsAsExpected() =
        runTest {
            val otherAccountId = 1
            every { keyStore.get("BottomNavItemsRepository.$otherAccountId.items", any<List<String>>()) } returns ITEMS_IDS
            every { keyStore.get("BottomNavItemsRepository.items", any<List<String>>()) } returns emptyList()

            val res = sut.get(null)

            assertEquals(BottomNavItemsRepository.DEFAULT_ITEMS, res)
            coVerify {
                keyStore.get("BottomNavItemsRepository.items", emptyList())
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
    }
}
