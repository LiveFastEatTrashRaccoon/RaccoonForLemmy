package com.livefast.eattrash.raccoonforlemmy.unit.userinfo.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.UserInfoViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal class UserInfoMMviModelParams(val userId: Long, val username: String, val otherInstance: String) :
    ViewModelCreationArgs

val userInfoModule =
    DI.Module("UserInfoModule") {
        bindViewModelWithArgs { args: UserInfoMMviModelParams ->
            UserInfoViewModel(
                userId = args.userId,
                username = args.username,
                otherInstance = args.otherInstance,
                userRepository = instance(),
                settingsRepository = instance(),
                itemCache = instance(),
                siteRepository = instance(),
            )
        }
    }
