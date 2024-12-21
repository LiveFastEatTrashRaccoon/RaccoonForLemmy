package com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.di

import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.SelectCommunityMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.SelectCommunityViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val selectCommunityModule =
    DI.Module("SelectCommunityModule") {
        bind<SelectCommunityMviModel> {
            provider {
                SelectCommunityViewModel(
                    settingsRepository = instance(),
                    communityPaginationManager = instance(),
                )
        }
    }
}
