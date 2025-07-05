package com.livefast.eattrash.raccoonforlemmy.unit.ban.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.ban.BanUserViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal data class BanUserMviModelParams(
    val userId: Long,
    val communityId: Long,
    val newValue: Boolean,
    val postId: Long,
    val commentId: Long,
) : ViewModelCreationArgs

val banModule =
    DI.Module("BanModule") {
        bindViewModelWithArgs { params: BanUserMviModelParams ->
            BanUserViewModel(
                userId = params.userId,
                communityId = params.communityId,
                newValue = params.newValue,
                postId = params.postId,
                commentId = params.commentId,
                identityRepository = instance(),
                communityRepository = instance(),
                notificationCenter = instance(),
            )
        }
    }
