package com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInts
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper

internal class DefaultLogoutUseCase(
    private val identityRepository: IdentityRepository,
    private val accountRepository: AccountRepository,
    private val notificationCenter: NotificationCenter,
    private val settingsRepository: SettingsRepository,
    private val communitySortRepository: CommunitySortRepository,
    private val bottomNavItemsRepository: BottomNavItemsRepository,
    private val lemmyValueCache: LemmyValueCache,
    private val userTagHelper: UserTagHelper,
    private val userSortRepository: UserSortRepository,
    private val postLastSeenDateRepository: PostLastSeenDateRepository,
) : LogoutUseCase {
    override suspend operator fun invoke() {
        notificationCenter.send(NotificationCenterEvent.ResetExplore)
        notificationCenter.send(NotificationCenterEvent.ResetHome)

        identityRepository.clearToken()
        communitySortRepository.clear()
        userSortRepository.clear()
        postLastSeenDateRepository.clear()
        lemmyValueCache.refresh()
        notificationCenter.send(NotificationCenterEvent.Logout)

        val oldAccountId = accountRepository.getActive()?.id
        if (oldAccountId != null) {
            accountRepository.setActive(oldAccountId, false)
        }
        val anonSettings = settingsRepository.getSettings(null)
        settingsRepository.changeCurrentSettings(anonSettings)

        val bottomBarSections = bottomNavItemsRepository.get(null)
        settingsRepository.changeCurrentBottomBarSections(bottomBarSections.toInts())

        userTagHelper.clear()
    }
}
