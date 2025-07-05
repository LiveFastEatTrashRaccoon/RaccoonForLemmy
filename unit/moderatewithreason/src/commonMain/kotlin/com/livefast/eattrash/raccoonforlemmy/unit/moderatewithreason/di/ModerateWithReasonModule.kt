package com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal data class ModerateWithReasonMviModelParams(val actionId: Int, val contentId: Long) : ViewModelCreationArgs

val moderateWithReasonModule =
    DI.Module("ModerateWithReasonModule") {
        bindViewModelWithArgs { params: ModerateWithReasonMviModelParams ->
            ModerateWithReasonViewModel(
                actionId = params.actionId,
                contentId = params.contentId,
                identityRepository = instance(),
                postRepository = instance(),
                commentRepository = instance(),
                userRepository = instance(),
                communityRepository = instance(),
            )
        }
    }
