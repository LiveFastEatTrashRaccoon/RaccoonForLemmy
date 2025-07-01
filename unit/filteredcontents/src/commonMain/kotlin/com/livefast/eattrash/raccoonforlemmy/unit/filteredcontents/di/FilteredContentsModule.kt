package com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class FilteredContentsMviModelParams(val contentsType: Int) : ViewModelCreationArgs

val filteredContentsModule =
    DI.Module("FilteredContentsModule") {
        bindViewModelWithArgs { args: FilteredContentsMviModelParams ->
            FilteredContentsViewModel(
                contentsType = args.contentsType,
                postPaginationManager = instance(),
                commentPaginationManager = instance(),
                themeRepository = instance(),
                settingsRepository = instance(),
                identityRepository = instance(),
                postRepository = instance(),
                commentRepository = instance(),
                accountRepository = instance(),
                userTagRepository = instance(),
                imagePreloadManager = instance(),
                hapticFeedback = instance(),
                notificationCenter = instance(),
                postNavigationManager = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
