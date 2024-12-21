package com.livefast.eattrash.raccoonforlemmy.core.l10n.di

import com.livefast.eattrash.raccoonforlemmy.core.l10n.DefaultL10nManager
import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val l10nModule =
    DI.Module("L10nModule") {
        bind<L10nManager> {
            singleton {
                DefaultL10nManager()
            }
    }
}
