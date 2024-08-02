package com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.di

import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityViewModel
import org.koin.dsl.module

val editCommunityModule =
    module {
        factory<EditCommunityMviModel> { params ->
            EditCommunityViewModel(
                communityId = params[0],
                identityRepository = get(),
                communityRepository = get(),
                mediaRepository = get(),
                notificationCenter = get(),
            )
        }
    }
