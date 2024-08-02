package com.livefast.eattrash.raccoonforlemmy.unit.explore.di

import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreViewModel
import org.koin.dsl.module

val exploreModule =
    module {
        factory<ExploreMviModel> { params ->
            ExploreViewModel(
                otherInstance = params[0],
                apiConfigRepository = get(),
                identityRepository = get(),
                paginationManager = get(),
                communityRepository = get(),
                postRepository = get(),
                commentRepository = get(),
                themeRepository = get(),
                settingsRepository = get(),
                notificationCenter = get(),
                hapticFeedback = get(),
                getSortTypesUseCase = get(),
                lemmyValueCache = get(),
                imagePreloadManager = get(),
            )
        }
    }
