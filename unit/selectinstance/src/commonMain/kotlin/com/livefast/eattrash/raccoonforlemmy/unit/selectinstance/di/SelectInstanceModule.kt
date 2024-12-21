package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.di

import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val selectInstanceModule =
    DI.Module("SelectInstanceModule") {
        bind<SelectInstanceMviModel> {
            provider {
                SelectInstanceViewModel(
                    instanceRepository = instance(),
                    communityRepository = instance(),
                    apiConfigurationRepository = instance(),
                    hapticFeedback = instance(),
            )
        }
    }
}
