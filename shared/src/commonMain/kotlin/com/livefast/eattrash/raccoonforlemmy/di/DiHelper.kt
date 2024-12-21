package com.livefast.eattrash.raccoonforlemmy.di

import com.livefast.eattrash.raccoonforlemmy.core.api.di.apiModule
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.appearanceModule
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.lemmyUiModule
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.l10nModule
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.navigationModule
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.notificationsModule
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.persistenceModule
import com.livefast.eattrash.raccoonforlemmy.core.preferences.di.preferencesModule
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.utilsModule
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.identityModule
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.di.inboxModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.di.lemmyPaginationModule
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di.lemmyRepositoryModule
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.di.inboxTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.profile.di.profileTabModule
import com.livefast.eattrash.raccoonforlemmy.feature.settings.di.settingsTabModule
import org.kodein.di.DI

fun initDi(additionalBuilder: DI.Builder.() -> Unit = {}) {
    RootDI.di =
        DI {
            additionalBuilder()

            // core modules
            importAll(
                apiModule,
                appearanceModule,
                l10nModule,
                lemmyUiModule,
                navigationModule,
                notificationsModule,
                persistenceModule,
                preferencesModule,
                utilsModule,
            )

            // domain
            importAll(
                identityModule,
                inboxModule,
                lemmyPaginationModule,
                lemmyRepositoryModule,
            )

            // features
            importAll(
                inboxTabModule,
                profileTabModule,
                settingsTabModule,
            )

            // shared
            importAll(
                detailOpenerModule,
                mainModule,
                sharedResourcesModule,
            )
        }
}
