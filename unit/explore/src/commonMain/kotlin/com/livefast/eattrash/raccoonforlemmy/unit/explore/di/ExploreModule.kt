package com.livefast.eattrash.raccoonforlemmy.unit.explore.di

import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val exploreModule =
    DI.Module("ExploreModule") {
        bind<ExploreMviModel> {
            factory { otherInstance: String ->
                ExploreViewModel(
                    otherInstance = otherInstance,
                    apiConfigRepository = instance(),
                    identityRepository = instance(),
                    communityRepository = instance(),
                    paginationManager = instance(),
                    postRepository = instance(),
                    commentRepository = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    notificationCenter = instance(),
                    hapticFeedback = instance(),
                    getSortTypesUseCase = instance(),
                    imagePreloadManager = instance(),
                    lemmyValueCache = instance(),
            )
        }
    }
}
