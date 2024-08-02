package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.di

import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceViewModel
import org.koin.dsl.module

val selectInstanceModule =
    module {
        factory<SelectInstanceMviModel> {
            SelectInstanceViewModel(
                instanceRepository = get(),
                communityRepository = get(),
                apiConfigurationRepository = get(),
                hapticFeedback = get(),
            )
        }
    }
