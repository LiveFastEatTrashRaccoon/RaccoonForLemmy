package com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di

import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoViewModel
import org.koin.dsl.module

val instanceInfoModule =
    module {
        factory<InstanceInfoMviModel> {
            InstanceInfoViewModel(
                url = it[0],
                siteRepository = get(),
                settingsRepository = get(),
                notificationCenter = get(),
                getSortTypesUseCase = get(),
                communityPaginationManager = get(),
            )
        }
    }
