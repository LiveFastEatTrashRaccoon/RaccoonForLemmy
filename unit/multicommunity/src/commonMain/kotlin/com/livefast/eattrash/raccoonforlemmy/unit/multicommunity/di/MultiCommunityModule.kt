package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.di

import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor.MultiCommunityEditorMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor.MultiCommunityEditorViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val multiCommunityModule =
    DI.Module("MultiCommunityModule") {
        bind<MultiCommunityMviModel> {
            factory { communityId: Long ->
                MultiCommunityViewModel(
                    communityId = communityId,
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
        }
        bind<MultiCommunityEditorMviModel> {
            factory { communityId: Long ->
                MultiCommunityEditorViewModel(
                    communityId = communityId,
                    accountRepository = instance(),
                    multiCommunityRepository = instance(),
                    communityPaginationManager = instance(),
                    notificationCenter = instance(),
                    settingsRepository = instance(),
                )
            }
        }
    }
