package com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val manageSubscriptionsModule =
    DI.Module("ManageSubscriptionsModule") {
        bindViewModel {
            ManageSubscriptionsViewModel(
                identityRepository = instance(),
                communityRepository = instance(),
                accountRepository = instance(),
                multiCommunityRepository = instance(),
                settingsRepository = instance(),
                favoriteCommunityRepository = instance(),
                communityPaginationManager = instance(),
                hapticFeedback = instance(),
                notificationCenter = instance(),
            )
        }
    }
