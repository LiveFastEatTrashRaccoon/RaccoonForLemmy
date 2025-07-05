package com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.CommunityInfoViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal data class CommunityInfoMviModelParams(
    val communityId: Long,
    val communityName: String,
    val otherInstance: String,
) : ViewModelCreationArgs

val communityInfoModule =
    DI.Module("CommunityInfoModule") {
        bindViewModelWithArgs { args: CommunityInfoMviModelParams ->
            CommunityInfoViewModel(
                communityId = args.communityId,
                communityName = args.communityName,
                otherInstance = args.otherInstance,
                communityRepository = instance(),
                settingsRepository = instance(),
                itemCache = instance(),
            )
        }
    }
