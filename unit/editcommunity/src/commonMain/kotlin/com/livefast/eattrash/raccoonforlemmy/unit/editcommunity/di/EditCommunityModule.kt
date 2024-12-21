package com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.di

import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val editCommunityModule =
    DI.Module("EditCommunityModule") {
        bind<EditCommunityMviModel> {
            factory { communityId: Long ->
                EditCommunityViewModel(
                    communityId = communityId,
                    identityRepository = instance(),
                    communityRepository = instance(),
                    mediaRepository = instance(),
                    notificationCenter = instance(),
            )
        }
    }
}
