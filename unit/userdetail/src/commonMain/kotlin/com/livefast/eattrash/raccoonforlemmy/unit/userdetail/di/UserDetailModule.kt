package com.livefast.eattrash.raccoonforlemmy.unit.userdetail.di

import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.UserDetailMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.UserDetailViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class UserDetailMviModelParams(
    val userId: Long,
    val otherInstance: String,
)

val userDetailModule =
    DI.Module("UserDetailModule") {
        bind<UserDetailMviModel> {
            factory { params: UserDetailMviModelParams ->
                UserDetailViewModel(
                    userId = params.userId,
                    otherInstance = params.otherInstance,
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
                )
            }
        }
    }
