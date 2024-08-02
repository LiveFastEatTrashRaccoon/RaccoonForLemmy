package com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di

import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonViewModel
import org.koin.dsl.module

val moderateWithReasonModule =
    module {
        factory<ModerateWithReasonMviModel> { params ->
            ModerateWithReasonViewModel(
                actionId = params[0],
                contentId = params[1],
                identityRepository = get(),
                postRepository = get(),
                commentRepository = get(),
                userRepository = get(),
                communityRepository = get(),
            )
        }
    }
