package com.livefast.eattrash.raccoonforlemmy.unit.usertags.di

import com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail.UserTagDetailMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail.UserTagDetailViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.list.UserTagsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.list.UserTagsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider

val userTagsModule =
    DI.Module("UserTagsModule") {
        bind<UserTagsMviModel> {
            provider {
                UserTagsViewModel(
                    accountRepository = instance(),
                    userTagRepository = instance(),
                )
            }
        }
        bind<UserTagDetailMviModel> {
            factory { id: Long ->
                UserTagDetailViewModel(
                    tagId = id,
                    userTagRepository = instance(),
                )
            }
        }
    }
