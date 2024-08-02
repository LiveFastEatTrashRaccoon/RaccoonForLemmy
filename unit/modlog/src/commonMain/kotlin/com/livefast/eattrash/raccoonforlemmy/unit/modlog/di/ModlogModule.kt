package com.livefast.eattrash.raccoonforlemmy.unit.modlog.di

import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogViewModel
import org.koin.dsl.module

val modlogModule =
    module {
        factory<ModlogMviModel> { params ->
            ModlogViewModel(
                communityId = params[0],
                themeRepository = get(),
                identityRepository = get(),
                modlogRepository = get(),
                settingsRepository = get(),
            )
        }
    }
