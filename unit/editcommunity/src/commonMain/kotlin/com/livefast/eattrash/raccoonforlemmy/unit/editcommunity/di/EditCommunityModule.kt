package com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class EditCommunityMviModelParams(val communityId: Long) : ViewModelCreationArgs

val editCommunityModule =
    DI.Module("EditCommunityModule") {
        bindViewModelWithArgs { args: EditCommunityMviModelParams ->
            EditCommunityViewModel(
                communityId = args.communityId,
                identityRepository = instance(),
                communityRepository = instance(),
                mediaRepository = instance(),
                notificationCenter = instance(),
            )
        }
    }
