package com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di

import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsViewModel
import org.koin.dsl.module

val filteredContentsModule =
    module {
        factory<FilteredContentsMviModel> { params ->
            FilteredContentsViewModel(
                contentsType = params[0],
                themeRepository = get(),
                settingsRepository = get(),
                identityRepository = get(),
                postRepository = get(),
                commentRepository = get(),
                hapticFeedback = get(),
                imagePreloadManager = get(),
                notificationCenter = get(),
                postPaginationManager = get(),
                commentPaginationManager = get(),
                postNavigationManager = get(),
                lemmyValueCache = get(),
            )
        }
    }
