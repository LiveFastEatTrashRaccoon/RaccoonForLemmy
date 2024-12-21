package com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.di

import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.CommunityInfoMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.CommunityInfoViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class CommunityInfoMviModelParams(
    val communityId: Long,
    val communityName: String,
    val otherInstance: String,
)

val communityInfoModule =
    DI.Module("CommunityInfoModule") {
        bind<CommunityInfoMviModel> {
            factory { params: CommunityInfoMviModelParams ->
                CommunityInfoViewModel(
                    communityId = params.communityId,
                    communityName = params.communityName,
                    otherInstance = params.otherInstance,
                    communityRepository = instance(),
                    settingsRepository = instance(),
                    itemCache = instance(),
                )
            }
        }
    }
