package com.github.diegoberaldin.raccoonforlemmy.di

import com.github.diegoberaldin.raccoonforlemmy.core.api.di.coreApiModule
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.coreAppearanceModule
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.coreNotificationModule
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.corePersistenceModule
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.di.corePreferencesModule
import com.github.diegoberaldin.raccoonforlemmy.core.utils.galleryHelperModule
import com.github.diegoberaldin.raccoonforlemmy.core.utils.hapticFeedbackModule
import com.github.diegoberaldin.raccoonforlemmy.core.utils.shareHelperModule
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.di.coreIdentityModule
import com.github.diegoberaldin.raccoonforlemmy.feature.home.di.homeTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di.inboxTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.di.profileTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature.search.di.searchTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.di.settingsTabModule
import com.github.diegoberaldin.raccoonforlemmy.resources.di.localizationModule
import org.koin.dsl.module

val sharedHelperModule = module {
    includes(
        internalSharedModule,
        coreAppearanceModule,
        corePreferencesModule,
        coreApiModule,
        coreIdentityModule,
        coreNotificationModule,
        corePersistenceModule,
        hapticFeedbackModule,
        localizationModule,
        shareHelperModule,
        galleryHelperModule,
        homeTabModule,
        inboxTabModule,
        profileTabModule,
        searchTabModule,
        settingsTabModule,
    )
}
