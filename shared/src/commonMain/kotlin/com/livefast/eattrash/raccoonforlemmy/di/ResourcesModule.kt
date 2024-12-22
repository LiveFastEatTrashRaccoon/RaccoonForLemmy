package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.l10n.Strings
import com.livefast.eattrash.raccoonforlemmy.core.resources.CoreResources
import com.livefast.eattrash.raccoonforlemmy.resources.SharedResources
import com.livefast.eattrash.raccoonforlemmy.resources.SharedStrings
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.singleton

internal val sharedResourcesModule =
    DI.Module("SharedResourcesModule") {
        bind<CoreResources> {
            singleton {
                SharedResources()
            }
        }
        bind<Strings> {
            factory { _: String ->
                SharedStrings()
            }
        }
    }
