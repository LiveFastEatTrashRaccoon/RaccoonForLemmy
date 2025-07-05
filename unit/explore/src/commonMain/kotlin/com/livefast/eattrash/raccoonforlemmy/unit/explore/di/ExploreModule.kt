package com.livefast.eattrash.raccoonforlemmy.unit.explore.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class ExploreMviModelParams(val otherInstance: String) : ViewModelCreationArgs

val exploreModule =
    DI.Module("ExploreModule") {
        bindViewModelWithArgs { args: ExploreMviModelParams ->
            ExploreViewModel(
                otherInstance = args.otherInstance,
                apiConfigRepository = instance(),
                identityRepository = instance(),
                communityRepository = instance(),
                paginationManager = instance(),
                postRepository = instance(),
                commentRepository = instance(),
                themeRepository = instance(),
                settingsRepository = instance(),
                userTagRepository = instance(),
                accountRepository = instance(),
                notificationCenter = instance(),
                hapticFeedback = instance(),
                getSortTypesUseCase = instance(),
                imagePreloadManager = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
