package com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di

import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val filteredContentsModule =
    DI.Module("FilteredContentsModule") {
        bind<FilteredContentsMviModel> {
            factory { contentsType: Int ->
                FilteredContentsViewModel(
                    contentsType = contentsType,
                    postPaginationManager = instance(),
                    commentPaginationManager = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    identityRepository = instance(),
                    postRepository = instance(),
                    commentRepository = instance(),
                    imagePreloadManager = instance(),
                    hapticFeedback = instance(),
                    notificationCenter = instance(),
                    postNavigationManager = instance(),
                    lemmyValueCache = instance(),
            )
        }
    }
}
