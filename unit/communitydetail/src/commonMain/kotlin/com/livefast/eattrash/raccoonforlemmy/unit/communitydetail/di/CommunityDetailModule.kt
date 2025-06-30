package com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.communitydetail.CommunityDetailViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal data class CommunityDetailMviModelParams(val communityId: Long, val otherInstance: String) :
    ViewModelCreationArgs

val communityDetailModule =
    DI.Module("CommunityDetailModule") {
        bindViewModelWithArgs { args: CommunityDetailMviModelParams ->
            CommunityDetailViewModel(
                communityId = args.communityId,
                otherInstance = args.otherInstance,
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
                userTagRepository = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
