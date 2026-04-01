package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LoginResponse
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.CreateSpecialTagsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.AuthRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefaultLoginUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val authRepository = mockk<AuthRepository>()
    private val apiConfigurationRepository =
        mockk<ApiConfigurationRepository>(relaxUnitFun = true) {
            every { instance } returns MutableStateFlow("")
        }
    private val identityRepository = mockk<IdentityRepository>(relaxUnitFun = true)
    private val accountRepository = mockk<AccountRepository>(relaxUnitFun = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
    private val siteRepository = mockk<SiteRepository>(relaxUnitFun = true)
    private val communitySortRepository = mockk<CommunitySortRepository>(relaxUnitFun = true)
    private val communityPreferredLanguageRepository =
        mockk<CommunityPreferredLanguageRepository>(relaxUnitFun = true)
    private val userSortRepository = mockk<UserSortRepository>(relaxUnitFun = true)
    private val postLastSeenDateRepository = mockk<PostLastSeenDateRepository>(relaxUnitFun = true)
    private val bottomNavItemsRepository =
        mockk<BottomNavItemsRepository>(relaxUnitFun = true) {
            coEvery { get(accountId = any()) } returns BottomNavItemsRepository.DEFAULT_ITEMS
        }
    private val lemmyValueCache = mockk<LemmyValueCache>(relaxUnitFun = true)
    private val createSpecialTagsUseCase = mockk<CreateSpecialTagsUseCase>(relaxUnitFun = true)
    private val sut =
        DefaultLoginUseCase(
            authRepository = authRepository,
            apiConfigurationRepository = apiConfigurationRepository,
            identityRepository = identityRepository,
            accountRepository = accountRepository,
            settingsRepository = settingsRepository,
            siteRepository = siteRepository,
            communitySortRepository = communitySortRepository,
            communityPreferredLanguageRepository = communityPreferredLanguageRepository,
            bottomNavItemsRepository = bottomNavItemsRepository,
            lemmyValueCache = lemmyValueCache,
            createSpecialTagsUseCase = createSpecialTagsUseCase,
            userSortRepository = userSortRepository,
            postLastSeenDateRepository = postLastSeenDateRepository,
        )

    @Test
    fun givenNewAccount_whenExecute_thenInteractionsAreAsExpected() = runTest {
        val accountId = 1L
        val anonymousSettings = SettingsModel()
        coEvery {
            authRepository.login(any(), any())
        } returns Result.success(LoginResponse("fake-token", registrationCreated = false, verifyEmailSent = true))
        coEvery {
            siteRepository.getAccountSettings(any())
        } returns null
        coEvery {
            accountRepository.getBy(any(), any())
        } returns null
        coEvery {
            accountRepository.getActive()
        } returns null
        coEvery {
            accountRepository.createAccount(any())
        } returns accountId
        coEvery {
            settingsRepository.getSettings(any())
        } returns anonymousSettings

        val res = sut("fake-instance", "fake-username", "fake-password")

        assertTrue(res.isSuccess)
        coVerify {
            apiConfigurationRepository.changeInstance("fake-instance")
            authRepository.login("fake-username", "fake-password")
            siteRepository.getAccountSettings("fake-token")
            accountRepository.getBy("fake-username", "fake-instance")
            accountRepository.createAccount(
                withArg {
                    assertEquals("fake-username", it.username)
                },
            )
            accountRepository.setActive(accountId, true)
            settingsRepository.createSettings(anonymousSettings, accountId)
            settingsRepository.changeCurrentSettings(anonymousSettings)
            communitySortRepository.clear()
            userSortRepository.clear()
            postLastSeenDateRepository.clear()
        }
    }

    @Test
    fun givenPreviousDifferentAccount_whenExecute_thenInteractionsAreAsExpected() = runTest {
        val accountId = 2L
        val oldAccountId = 1L
        val anonymousSettings = SettingsModel()
        coEvery {
            authRepository.login(any(), any())
        } returns Result.success(LoginResponse("fake-token", registrationCreated = false, verifyEmailSent = true))
        coEvery {
            siteRepository.getAccountSettings(any())
        } returns null
        coEvery {
            accountRepository.getBy(any(), any())
        } returns null
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = oldAccountId,
                username = "old-username",
                instance = "old-instance",
                jwt = "old-token",
            )
        coEvery {
            accountRepository.createAccount(any())
        } returns accountId
        coEvery {
            settingsRepository.getSettings(any())
        } returns anonymousSettings

        val res = sut("fake-instance", "fake-username", "fake-password")

        assertTrue(res.isSuccess)
        coVerify {
            apiConfigurationRepository.changeInstance("fake-instance")
            authRepository.login("fake-username", "fake-password")
            siteRepository.getAccountSettings("fake-token")
            accountRepository.getBy("fake-username", "fake-instance")
            accountRepository.createAccount(
                withArg {
                    assertEquals("fake-username", it.username)
                },
            )
            accountRepository.setActive(oldAccountId, false)
            accountRepository.setActive(accountId, true)
            settingsRepository.createSettings(anonymousSettings, accountId)
            settingsRepository.changeCurrentSettings(anonymousSettings)
            communitySortRepository.clear()
            userSortRepository.clear()
            postLastSeenDateRepository.clear()
        }
    }

    @Test
    fun givenPreviousExistingAccount_whenExecute_thenInteractionsAreAsExpected() = runTest {
        val accountId = 2L
        val oldSettings = SettingsModel(id = 1)
        coEvery {
            authRepository.login(any(), any())
        } returns Result.success(LoginResponse("fake-token", registrationCreated = false, verifyEmailSent = true))
        coEvery {
            siteRepository.getAccountSettings(any())
        } returns null
        coEvery {
            accountRepository.getBy(any(), any())
        } returns
            AccountModel(
                id = accountId,
                username = "old-username",
                instance = "old-instance",
                jwt = "old-token",
            )
        coEvery {
            accountRepository.getActive()
        } returns null
        coEvery {
            accountRepository.createAccount(any())
        } returns accountId
        coEvery {
            settingsRepository.getSettings(accountId)
        } returns oldSettings

        val res = sut("fake-instance", "fake-username", "fake-password")

        assertTrue(res.isSuccess)
        coVerify {
            apiConfigurationRepository.changeInstance("fake-instance")
            authRepository.login("fake-username", "fake-password")
            siteRepository.getAccountSettings("fake-token")
            accountRepository.getBy("fake-username", "fake-instance")
            accountRepository.setActive(accountId, true)
            settingsRepository.changeCurrentSettings(oldSettings)
            communitySortRepository.clear()
            communityPreferredLanguageRepository.clear()
            userSortRepository.clear()
            postLastSeenDateRepository.clear()
        }
        coVerify(inverse = true) {
            accountRepository.createAccount(any())
            settingsRepository.createSettings(any(), any())
        }
    }

    @Test
    fun givenAuthFails_whenExecuted_thenExceptionIsThrown() = runTest {
        val errorMessage = "fake-error-message"
        coEvery {
            authRepository.login(any(), any())
        } returns Result.failure(Exception(errorMessage))

        val res = sut("fake-instance", "fake-username", "fake-password")

        assertTrue(res.isFailure)
        val exc = res.exceptionOrNull()
        assertEquals(errorMessage, exc?.message)
    }
}
