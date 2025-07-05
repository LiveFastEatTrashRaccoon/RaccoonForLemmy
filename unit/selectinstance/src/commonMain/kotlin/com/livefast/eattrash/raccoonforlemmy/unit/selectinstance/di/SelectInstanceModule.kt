package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val selectInstanceModule =
    DI.Module("SelectInstanceModule") {
        bindViewModel {
            SelectInstanceViewModel(
                instanceRepository = instance(),
                communityRepository = instance(),
                apiConfigurationRepository = instance(),
                hapticFeedback = instance(),
            )
        }
    }
