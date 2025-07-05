package com.livefast.eattrash.raccoonforlemmy.unit.usertags.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail.UserTagDetailViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.list.UserTagsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class UserTagDetailMviModelParams(val id: Long) : ViewModelCreationArgs

val userTagsModule =
    DI.Module("UserTagsModule") {
        bindViewModel {
            UserTagsViewModel(
                accountRepository = instance(),
                userTagRepository = instance(),
                userTagHelper = instance(),
            )
        }
        bindViewModelWithArgs { args: UserTagDetailMviModelParams ->
            UserTagDetailViewModel(
                tagId = args.id,
                userTagRepository = instance(),
            )
        }
    }
