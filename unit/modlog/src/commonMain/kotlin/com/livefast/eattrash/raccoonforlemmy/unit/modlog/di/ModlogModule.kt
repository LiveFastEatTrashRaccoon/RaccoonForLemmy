package com.livefast.eattrash.raccoonforlemmy.unit.modlog.di

import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val modlogModule =
    DI.Module("ModlogModule") {
        bind<ModlogMviModel> {
            factory { communityId: Long ->
                ModlogViewModel(
                    communityId = communityId,
                    themeRepository = instance(),
                    identityRepository = instance(),
                    modlogRepository = instance(),
                    settingsRepository = instance(),
                )
            }
        }
    }
