package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.AuthRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class DefaultLogoutUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val identityRepository = mockk<IdentityRepository>(relaxUnitFun = true)
    private val accountRepository = mockk<AccountRepository>(relaxUnitFun = true)
    private val authRepository = mockk<AuthRepository>(relaxUnitFun = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
    private val notificationCenter = mockk<NotificationCenter>(relaxUnitFun = true)
    private val communitySortRepository = mockk<CommunitySortRepository>(relaxUnitFun = true)
    private val lemmyValueCache = mockk<LemmyValueCache>(relaxUnitFun = true)
    private val userSortRepository = mockk<UserSortRepository>(relaxUnitFun = true)
    private val postLastSeenDateRepository = mockk<PostLastSeenDateRepository>(relaxUnitFun = true)
    private val bottomNavItemsRepository =
        mockk<BottomNavItemsRepository>(relaxUnitFun = true) {
            coEvery { get(accountId = any()) } returns BottomNavItemsRepository.DEFAULT_ITEMS
        }
    private val userTagHelper = mockk<UserTagHelper>(relaxUnitFun = true)
    private val sut =
        DefaultLogoutUseCase(
            identityRepository = identityRepository,
            accountRepository = accountRepository,
            authRepository = authRepository,
            settingsRepository = settingsRepository,
            notificationCenter = notificationCenter,
            communitySortRepository = communitySortRepository,
            bottomNavItemsRepository = bottomNavItemsRepository,
            lemmyValueCache = lemmyValueCache,
            userTagHelper = userTagHelper,
            userSortRepository = userSortRepository,
            postLastSeenDateRepository = postLastSeenDateRepository,
        )

    @Test
    fun givenSuccess_whenExecute_thenInteractionsAreAsExpected() = runTest {
        coEvery { authRepository.logout() } returns Result.success(Unit)
        val accountId = 1L
        coEvery {
            accountRepository.getActive()
        } returns
            AccountModel(
                id = accountId,
                instance = "fake-instance",
                username = "fake-username",
                jwt = "fake-token",
            )
        val anonymousSettings = SettingsModel()
        coEvery {
            settingsRepository.getSettings(any())
        } returns anonymousSettings

        sut()

        coVerify {
            authRepository.logout()
            notificationCenter.send(ofType(NotificationCenterEvent.ResetHome::class))
            notificationCenter.send(ofType(NotificationCenterEvent.ResetExplore::class))
            notificationCenter.send(ofType(NotificationCenterEvent.Logout::class))
            identityRepository.clearToken()
            accountRepository.setActive(accountId, false)
            settingsRepository.changeCurrentSettings(anonymousSettings)
            userTagHelper.clear()
            userSortRepository.clear()
            postLastSeenDateRepository.clear()
        }
    }
}
