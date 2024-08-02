package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.di

import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail.MultiCommunityViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor.MultiCommunityEditorMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor.MultiCommunityEditorViewModel
import org.koin.dsl.module

val multiCommunityModule =
    module {
        factory<MultiCommunityMviModel> { params ->
            MultiCommunityViewModel(
                communityId = params[0],
                postRepository = get(),
                identityRepository = get(),
                siteRepository = get(),
                themeRepository = get(),
                shareHelper = get(),
                settingsRepository = get(),
                notificationCenter = get(),
                hapticFeedback = get(),
                postPaginationManager = get(),
                imagePreloadManager = get(),
                getSortTypesUseCase = get(),
                multiCommunityRepository = get(),
                postNavigationManager = get(),
                lemmyValueCache = get(),
            )
        }
        factory<MultiCommunityEditorMviModel> { params ->
            MultiCommunityEditorViewModel(
                communityId = params[0],
                accountRepository = get(),
                multiCommunityRepository = get(),
                communityPaginationManager = get(),
                notificationCenter = get(),
                settingsRepository = get(),
            )
        }
    }
