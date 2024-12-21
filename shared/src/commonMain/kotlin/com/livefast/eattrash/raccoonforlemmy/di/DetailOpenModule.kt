package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.impl.DefaultDetailOpener
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal val detailOpenerModule =
    DI.Module("DetailOpenerModule") {
        bind<DetailOpener> {
            singleton {
                DefaultDetailOpener(
                    navigationCoordinator = instance(),
                    itemCache = instance(),
                    identityRepository = instance(),
                    communityRepository = instance(),
                )
            }
        }
    }
