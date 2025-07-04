package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor.MultiCommunityEditorViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class MultiCommunityMviModelParams(val communityId: Long) : ViewModelCreationArgs

data class MultiCommunityEditorMviModelParams(val communityId: Long) : ViewModelCreationArgs

val multiCommunityModule =
    DI.Module("MultiCommunityModule") {
        bindViewModelWithArgs { args: MultiCommunityMviModelParams ->
            MultiCommunityViewModel(
                communityId = args.communityId,
                postRepository = instance(),
                identityRepository = instance(),
                siteRepository = instance(),
                themeRepository = instance(),
                shareHelper = instance(),
                settingsRepository = instance(),
                accountRepository = instance(),
                userTagRepository = instance(),
                notificationCenter = instance(),
                hapticFeedback = instance(),
                postPaginationManager = instance(),
                imagePreloadManager = instance(),
                getSortTypesUseCase = instance(),
                multiCommunityRepository = instance(),
                postNavigationManager = instance(),
                lemmyValueCache = instance(),
            )
        }
        bindViewModelWithArgs { args: MultiCommunityEditorMviModelParams ->
            MultiCommunityEditorViewModel(
                communityId = args.communityId,
                accountRepository = instance(),
                multiCommunityRepository = instance(),
                communityPaginationManager = instance(),
                notificationCenter = instance(),
                settingsRepository = instance(),
            )
        }
    }
