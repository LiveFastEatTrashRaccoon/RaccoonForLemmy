package com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.di

import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.CommunityDetailMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.CommunityDetailViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class CommunityDetailMviModelParams(
    val communityId: Long,
    val otherInstance: String,
)

val communityDetailModule =
    DI.Module("CommunityDetailModule") {
        bind<CommunityDetailMviModel> {
            factory { params: CommunityDetailMviModelParams ->
                CommunityDetailViewModel(
                    communityId = params.communityId,
                    otherInstance = params.otherInstance,
                    identityRepository = instance(),
                    apiConfigurationRepository = instance(),
                    postPaginationManager = instance(),
                    communityRepository = instance(),
                    postRepository = instance(),
                    siteRepository = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    accountRepository = instance(),
                    favoriteCommunityRepository = instance(),
                    shareHelper = instance(),
                    hapticFeedback = instance(),
                    zombieModeHelper = instance(),
                    imagePreloadManager = instance(),
                    getSortTypesUseCase = instance(),
                    notificationCenter = instance(),
                    itemCache = instance(),
                    communitySortRepository = instance(),
                    postNavigationManager = instance(),
                    communityPreferredLanguageRepository = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
    }
