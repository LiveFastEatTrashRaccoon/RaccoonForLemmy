package com.livefast.eattrash.raccoonforlemmy.unit.drawer.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.cache.DefaultSubscriptionsCache
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.cache.SubscriptionsCache
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.content.ModalDrawerViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val drawerModule =
    DI.Module("DrawerModule") {
        bind<SubscriptionsCache> {
            singleton {
                DefaultSubscriptionsCache(
                    identityRepository = instance(),
                    communityPaginationManager = instance(),
                )
            }
        }
        bindViewModel {
            ModalDrawerViewModel(
                identityRepository = instance(),
                communityRepository = instance(),
                accountRepository = instance(),
                multiCommunityRepository = instance(),
                siteRepository = instance(),
                apiConfigurationRepository = instance(),
                settingsRepository = instance(),
                favoriteCommunityRepository = instance(),
                communityPaginationManager = instance(),
                notificationCenter = instance(),
                subscriptionsCache = instance(),
            )
        }
    }
