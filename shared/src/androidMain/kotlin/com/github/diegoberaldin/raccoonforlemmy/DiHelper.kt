package com.github.diegoberaldin.raccoonforlemmy

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.di.coreAppearanceModule
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.di.corePreferencesModule
import com.github.diegoberaldin.raccoonforlemmy.feature_inbox.inboxTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature_profile.profileTabModule
import com.github.diegoberaldin.raccoonforlemmy.feature_search.searchTabModule
import com.github.diegoberaldin.racoonforlemmy.feature_home.homeTabModule
import com.github.diegoberaldin.racoonforlemmy.feature_settings.settingsTabModule
import org.koin.dsl.module

val sharedHelperModule = module {
    includes(
        coreAppearanceModule,
        corePreferencesModule,
        homeTabModule,
        inboxTabModule,
        profileTabModule,
        searchTabModule,
        settingsTabModule,
    )
}