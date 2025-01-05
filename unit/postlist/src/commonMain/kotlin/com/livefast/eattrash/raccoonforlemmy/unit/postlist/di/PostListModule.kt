package com.livefast.eattrash.raccoonforlemmy.unit.postlist.di

import com.livefast.eattrash.raccoonforlemmy.unit.postlist.PostListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.PostListViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val postListModule =
    DI.Module("PostListModule") {
        bind<PostListMviModel> {
            provider {
                PostListViewModel(
                    postPaginationManager = instance(),
                    postRepository = instance(),
                    apiConfigurationRepository = instance(),
                    identityRepository = instance(),
                    siteRepository = instance(),
                    themeRepository = instance(),
                    shareHelper = instance(),
                    settingsRepository = instance(),
                    userRepository = instance(),
                    communityRepository = instance(),
                    accountRepository = instance(),
                    userTagRepository = instance(),
                    notificationCenter = instance(),
                    hapticFeedback = instance(),
                    zombieModeHelper = instance(),
                    imagePreloadManager = instance(),
                    getSortTypesUseCase = instance(),
                    postNavigationManager = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
    }
