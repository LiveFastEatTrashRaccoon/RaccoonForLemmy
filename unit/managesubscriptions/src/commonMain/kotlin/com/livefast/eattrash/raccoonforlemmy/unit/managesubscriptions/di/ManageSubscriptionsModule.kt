package com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.di

import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val manageSubscriptionsModule =
    DI.Module("ManageSubscriptionsModule") {
        bind<ManageSubscriptionsMviModel> {
            provider {
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
}
