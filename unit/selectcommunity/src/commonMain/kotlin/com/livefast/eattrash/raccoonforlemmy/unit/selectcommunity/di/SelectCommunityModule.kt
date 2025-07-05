package com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity.SelectCommunityViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val selectCommunityModule =
    DI.Module("SelectCommunityModule") {
        bindViewModel {
            SelectCommunityViewModel(
                settingsRepository = instance(),
                communityPaginationManager = instance(),
            )
        }
    }
