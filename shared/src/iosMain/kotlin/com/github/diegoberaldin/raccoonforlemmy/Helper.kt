package com.github.diegoberaldin.raccoonforlemmy

import com.github.diegoberaldin.raccoonforlemmy.example.di.greetingModule
import com.github.diegoberaldin.raccoonforlemmy.example.di.platformModule
import com.github.diegoberaldin.raccoonforlemmy.home.homeTabModule
import com.github.diegoberaldin.raccoonforlemmy.inbox.inboxTabModule
import com.github.diegoberaldin.raccoonforlemmy.profile.profileTabModule
import com.github.diegoberaldin.raccoonforlemmy.search.searchTabModule
import com.github.diegoberaldin.raccoonforlemmy.settings.settingsTabModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            greetingModule,
            platformModule,
            homeTabModule,
            inboxTabModule,
            profileTabModule,
            searchTabModule,
            settingsTabModule,
        )
    }
}