package com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di

import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val instanceInfoModule =
    DI.Module("InstanceInfoModule") {
        bind<InstanceInfoMviModel> {
            factory { url: String ->
                InstanceInfoViewModel(
                    url = url,
                    siteRepository = instance(),
                    settingsRepository = instance(),
                    notificationCenter = instance(),
                    getSortTypesUseCase = instance(),
                    communityPaginationManager = instance(),
            )
        }
    }
}
