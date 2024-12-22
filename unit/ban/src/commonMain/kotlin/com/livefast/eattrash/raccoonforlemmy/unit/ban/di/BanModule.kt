package com.livefast.eattrash.raccoonforlemmy.unit.ban.di

import com.livefast.eattrash.raccoonforlemmy.unit.ban.BanUserViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class BanUserMviModelParams(
    val userId: Long,
    val communityId: Long,
    val newValue: Boolean,
    val postId: Long,
    val commentId: Long,
)

val banModule =
    DI.Module("BanModule") {
        bind<BanUserViewModel> {
            factory { params: BanUserMviModelParams ->
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
    }
