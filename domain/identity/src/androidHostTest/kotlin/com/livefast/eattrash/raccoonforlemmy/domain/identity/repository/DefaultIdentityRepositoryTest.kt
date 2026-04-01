package com.livefast.eattrash.raccoonforlemmy.domain.identity.repository

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.core.utils.network.NetworkManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultIdentityRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val accountRepository = mockk<AccountRepository>()
    private val siteRepository = mockk<SiteRepository>()
    private val networkManager =
        mockk<NetworkManager> {
            coEvery { isNetworkAvailable() } returns true
        }

    private val sut =
        DefaultIdentityRepository(
            accountRepository = accountRepository,
            siteRepository = siteRepository,
            networkManager = networkManager,
        )

    @Test
    fun givenNetworkAvailableAndUserLogged_whenStartup_thenResultIsAsExpected() = runTest {
        val fakeToken = "fake-token"
        val fakeAccount =
            mockk<AccountModel> {
                every { jwt } returns fakeToken
            }
        val fakeUser = UserModel(id = 1)
        coEvery { accountRepository.getActive() } returns fakeAccount
        coEvery { siteRepository.getCurrentUser(any()) } returns fakeUser

        sut.startup()

        val token = sut.authToken.value
        assertEquals(fakeToken, token)
        val isLogged = sut.isLogged.value
        assertTrue(isLogged == true)
        assertEquals(fakeUser, sut.cachedUser)

        coVerify {
            accountRepository.getActive()
            networkManager.isNetworkAvailable()
            siteRepository.getCurrentUser("fake-token")
        }
    }

    @Test
    fun givenNetworkUnavailable_whenStartup_thenResultIsAsExpected() = runTest {
        coEvery { networkManager.isNetworkAvailable() } returns false
        val fakeToken = "fake-token"
        val fakeAccount =
            mockk<AccountModel> {
                every { jwt } returns fakeToken
            }
        val fakeUser = UserModel(id = 1)
        coEvery { accountRepository.getActive() } returns fakeAccount
        coEvery { siteRepository.getCurrentUser(any()) } returns fakeUser

        sut.startup()

        val token = sut.authToken.value
        assertEquals(fakeToken, token)
        val isLogged = sut.isLogged.value
        assertNull(isLogged)

        coVerify {
            accountRepository.getActive()
            networkManager.isNetworkAvailable()
            siteRepository wasNot Called
        }
    }

    @Test
    fun givenNotUserLogged_whenStartup_thenResultIsAsExpected() = runTest {
        coEvery { accountRepository.getActive() } returns null

        sut.startup()

        val token = sut.authToken.value
        assertEquals("", token)
        val isLogged = sut.isLogged.value
        assertTrue(isLogged == false)
        assertNull(sut.cachedUser)

        coVerify {
            accountRepository.getActive()
            networkManager wasNot Called
            siteRepository wasNot Called
        }
    }
}
