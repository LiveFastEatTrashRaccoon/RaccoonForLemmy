package com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.di

import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.SelectCommunityMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.SelectCommunityViewModel
import org.koin.dsl.module

val selectCommunityModule =
    module {
        factory<SelectCommunityMviModel> {
            SelectCommunityViewModel(
                settingsRepository = get(),
                communityPaginationManager = get(),
            )
        }
    }
