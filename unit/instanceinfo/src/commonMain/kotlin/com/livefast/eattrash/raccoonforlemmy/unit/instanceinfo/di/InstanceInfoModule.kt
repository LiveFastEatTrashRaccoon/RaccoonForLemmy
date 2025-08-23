package com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class InstanceInfoMviModelParams(val url: String) : ViewModelCreationArgs

val instanceInfoModule =
    DI.Module("InstanceInfoModule") {
        bindViewModelWithArgs { args: InstanceInfoMviModelParams ->
            InstanceInfoViewModel(
                url = args.url,
                siteRepository = instance(),
                settingsRepository = instance(),
                getSortTypesUseCase = instance(),
                communityPaginationManager = instance(),
            )
        }
    }
