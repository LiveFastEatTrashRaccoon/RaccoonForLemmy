package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class DefaultSwitchAccountUseCaseTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val identityRepository = mockk<IdentityRepository>(relaxUnitFun = true)
    private val accountRepository = mockk<AccountRepository>(relaxUnitFun = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
    private val serviceProvider = mockk<ServiceProvider>(relaxUnitFun = true)
    private val notificationCenter = mockk<NotificationCenter>(relaxUnitFun = true)
    private val communitySortRepository = mockk<CommunitySortRepository>(relaxUnitFun = true)
    private val communityPreferredLanguageRepository =
        mockk<CommunityPreferredLanguageRepository>(relaxUnitFun = true)
    private val userSortRepository = mockk<UserSortRepository>(relaxUnitFun = true)
    private val postLastSeenDateRepository = mockk<PostLastSeenDateRepository>(relaxUnitFun = true)
    private val lemmyValueCache = mockk<LemmyValueCache>(relaxUnitFun = true)
    private val bottomNavItemsRepository =
        mockk<BottomNavItemsRepository>(relaxUnitFun = true) {
            coEvery { get(accountId = any()) } returns BottomNavItemsRepository.DEFAULT_ITEMS
        }
    private val userTagHelper = mockk<UserTagHelper>(relaxUnitFun = true)
    private val sut =
        DefaultSwitchAccountUseCase(
            identityRepository = identityRepository,
            accountRepository = accountRepository,
            settingsRepository = settingsRepository,
            serviceProvider = serviceProvider,
            notificationCenter = notificationCenter,
            communitySortRepository = communitySortRepository,
            communityPreferredLanguageRepository = communityPreferredLanguageRepository,
            bottomNavItemsRepository = bottomNavItemsRepository,
            lemmyValueCache = lemmyValueCache,
            userTagHelper = userTagHelper,
            userSortRepository = userSortRepository,
            postLastSeenDateRepository = postLastSeenDateRepository,
        )

    @Test
    fun whenExecute_thenInteractionsAreAsExpected() =
        runTest {
            val accountId = 2L
            val oldAccountId = 1L
            val newAccount =
                AccountModel(
                    id = accountId,
                    username = "new-username",
                    instance = "new-instance",
                    jwt = "new-token",
                )
            val oldAccount =
                AccountModel(
                    id = oldAccountId,
                    username = "old-username",
                    instance = "old-instance",
                    jwt = "old-token",
                )
            val oldSettings = SettingsModel(id = 1)
            val newSettings = SettingsModel(id = 2)
            coEvery {
                accountRepository.getBy(any(), any())
            } returns null
            coEvery {
                accountRepository.getActive()
            } returns oldAccount
            coEvery {
                settingsRepository.getSettings(oldAccountId)
            } returns oldSettings
            coEvery {
                settingsRepository.getSettings(accountId)
            } returns newSettings

            sut(newAccount)

            coVerify {
                accountRepository.setActive(oldAccountId, false)
                accountRepository.setActive(accountId, true)
                settingsRepository.getSettings(accountId)
                settingsRepository.changeCurrentSettings(newSettings)
                communitySortRepository.clear()
                communityPreferredLanguageRepository.clear()
                notificationCenter.send(ofType(NotificationCenterEvent.Logout::class))
                identityRepository.storeToken("new-token")
                identityRepository.refreshLoggedState()
                serviceProvider.changeInstance("new-instance")
                userTagHelper.clear()
                userSortRepository.clear()
                postLastSeenDateRepository.clear()
            }
        }
}
