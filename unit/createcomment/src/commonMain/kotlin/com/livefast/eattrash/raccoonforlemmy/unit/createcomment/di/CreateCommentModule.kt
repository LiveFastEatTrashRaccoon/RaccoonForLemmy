package com.livefast.eattrash.raccoonforlemmy.unit.createcomment.di

import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.CreateCommentMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.CreateCommentViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class CreateCommentMviModelParams(
    val postId: Long,
    val parentId: Long,
    val editedCommentId: Long,
    val draftId: Long,
)

val createCommentModule =
    DI.Module("CreateCommentModule") {
        bind<CreateCommentMviModel> {
            factory { params: CreateCommentMviModelParams ->
                CreateCommentViewModel(
                    postId = params.postId,
                    parentId = params.parentId,
                    editedCommentId = params.editedCommentId,
                    draftId = params.draftId,
                    identityRepository = instance(),
                    commentRepository = instance(),
                    postRepository = instance(),
                    mediaRepository = instance(),
                    siteRepository = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    notificationCenter = instance(),
                    itemCache = instance(),
                    accountRepository = instance(),
                    draftRepository = instance(),
                    communityPreferredLanguageRepository = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
    }
