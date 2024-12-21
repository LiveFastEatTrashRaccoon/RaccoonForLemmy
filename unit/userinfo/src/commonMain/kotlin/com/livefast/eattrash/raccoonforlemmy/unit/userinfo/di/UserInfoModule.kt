package com.livefast.eattrash.raccoonforlemmy.unit.userinfo.di

import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.UserInfoMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.UserInfoViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal class UserInfoMMviModelParams(
    val userId: Long,
    val username: String,
    val otherInstance: String,
)

val userInfoModule =
    DI.Module("UserInfoModule") {
        bind<UserInfoMviModel> {
            factory { params: UserInfoMMviModelParams ->
                UserInfoViewModel(
                    userId = params.userId,
                    username = params.username,
                    otherInstance = params.otherInstance,
                    userRepository = instance(),
                    settingsRepository = instance(),
                    itemCache = instance(),
                    siteRepository = instance(),
                )
            }
        }
    }
