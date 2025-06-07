package com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.di

import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class ModerateWithReasonMviModelParams(val actionId: Int, val contentId: Long)

val moderateWithReasonModule =
    DI.Module("ModerateWithReasonModule") {
        bind<ModerateWithReasonMviModel> {
            factory { params: ModerateWithReasonMviModelParams ->
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
    }
