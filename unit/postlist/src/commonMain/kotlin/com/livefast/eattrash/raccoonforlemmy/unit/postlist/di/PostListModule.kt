package com.livefast.eattrash.raccoonforlemmy.unit.postlist.di

import com.livefast.eattrash.raccoonforlemmy.unit.postlist.PostListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.PostListViewModel
import org.koin.dsl.module

val postListModule =
    module {
        factory<PostListMviModel> {
            PostListViewModel(
                postRepository = get(),
                apiConfigurationRepository = get(),
                identityRepository = get(),
                siteRepository = get(),
                themeRepository = get(),
                settingsRepository = get(),
                userRepository = get(),
                communityRepository = get(),
                shareHelper = get(),
                notificationCenter = get(),
                hapticFeedback = get(),
                zombieModeHelper = get(),
                imagePreloadManager = get(),
                getSortTypesUseCase = get(),
                postPaginationManager = get(),
                postNavigationManager = get(),
                lemmyValueCache = get(),
            )
        }
    }
