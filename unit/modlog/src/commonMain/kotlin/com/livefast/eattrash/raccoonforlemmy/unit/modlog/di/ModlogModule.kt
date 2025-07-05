package com.livefast.eattrash.raccoonforlemmy.unit.modlog.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class ModlogMviModelParams(val communityId: Long) : ViewModelCreationArgs

val modlogModule =
    DI.Module("ModlogModule") {
        bindViewModelWithArgs { args: ModlogMviModelParams ->
            ModlogViewModel(
                communityId = args.communityId,
                themeRepository = instance(),
                identityRepository = instance(),
                modlogRepository = instance(),
                settingsRepository = instance(),
            )
        }
    }
