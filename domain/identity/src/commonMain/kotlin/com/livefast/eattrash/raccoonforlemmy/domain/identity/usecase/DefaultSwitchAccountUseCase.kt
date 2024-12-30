package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInts
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.AccountModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper

internal class DefaultSwitchAccountUseCase(
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val serviceProvider: ServiceProvider,
    private val notificationCenter: NotificationCenter,
    private val settingsRepository: SettingsRepository,
    private val communitySortRepository: CommunitySortRepository,
    private val communityPreferredLanguageRepository: CommunityPreferredLanguageRepository,
    private val bottomNavItemsRepository: BottomNavItemsRepository,
    private val lemmyValueCache: LemmyValueCache,
    private val userTagHelper: UserTagHelper,
) : SwitchAccountUseCase {
    override suspend fun invoke(account: AccountModel) {
        val accountId = account.id ?: return
        val jwt = account.jwt.takeIf { it.isNotEmpty() } ?: return
        val instance = account.instance.takeIf { it.isNotEmpty() } ?: return
        val oldActiveAccountId =
            accountRepository.getActive()?.id.takeIf { it != accountId } ?: return

        accountRepository.setActive(oldActiveAccountId, false)
        accountRepository.setActive(accountId, true)

        notificationCenter.send(NotificationCenterEvent.Logout)

        communitySortRepository.clear()
        communityPreferredLanguageRepository.clear()

        serviceProvider.changeInstance(instance)

        identityRepository.storeToken(jwt)
        identityRepository.refreshLoggedState()
        lemmyValueCache.refresh(jwt)

        val newSettings = settingsRepository.getSettings(accountId)
        settingsRepository.changeCurrentSettings(newSettings)

        val bottomBarSections = bottomNavItemsRepository.get(accountId)
        settingsRepository.changeCurrentBottomBarSections(bottomBarSections.toInts())

        userTagHelper.clear()
    }
}
