package com.livefast.eattrash.raccoonforlemmy.unit.userdetail.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.UserDetailViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal data class UserDetailMviModelParams(val userId: Long, val otherInstance: String) : ViewModelCreationArgs

val userDetailModule =
    DI.Module("UserDetailModule") {
        bindViewModelWithArgs { args: UserDetailMviModelParams ->
            UserDetailViewModel(
                userId = args.userId,
                otherInstance = args.otherInstance,
                identityRepository = instance(),
                apiConfigurationRepository = instance(),
                postPaginationManager = instance(),
                commentPaginationManager = instance(),
                userRepository = instance(),
                postRepository = instance(),
                commentRepository = instance(),
                siteRepository = instance(),
                themeRepository = instance(),
                shareHelper = instance(),
                hapticFeedback = instance(),
                settingsRepository = instance(),
                userTagRepository = instance(),
                userTagHelper = instance(),
                accountRepository = instance(),
                notificationCenter = instance(),
                imagePreloadManager = instance(),
                getSortTypesUseCase = instance(),
                itemCache = instance(),
                postNavigationManager = instance(),
                lemmyValueCache = instance(),
                userSortRepository = instance(),
            )
        }
    }
