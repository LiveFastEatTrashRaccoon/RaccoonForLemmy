package com.github.diegoberaldin.raccoonforlemmy

import com.github.diegoberaldin.raccoonforlemmy.core_api.di.coreApiModule
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.di.coreAppearanceModule
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.di.coreIdentityModule
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.di.corePreferencesModule
import com.github.diegoberaldin.raccoonforlemmy.feature_inbox.inboxTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.di.profileTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature_search.searchTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature_home.di.homeTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.di.settingsTabModule
import com.github.diegoberaldin.raccoonforlemmy.resources.di.localizationModule
import org.koin.dsl.module

val sharedHelperModule = module {
    includes(
        coreAppearanceModule,
        corePreferencesModule,
        coreApiModule,
        coreIdentityModule,
        localizationModule,
        homeTabModule,
        inboxTabModule,
        profileTabModule,
        searchTabModule,
        settingsTabModule,
    )
}