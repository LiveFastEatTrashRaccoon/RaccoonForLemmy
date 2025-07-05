package com.livefast.eattrash.raccoonforlemmy.unit.createcomment.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.createcomment.CreateCommentViewModel
import org.kodein.di.DI
import org.kodein.di.instance

internal data class CreateCommentMviModelParams(
    val postId: Long,
    val parentId: Long,
    val editedCommentId: Long,
    val draftId: Long,
) : ViewModelCreationArgs

val createCommentModule =
    DI.Module("CreateCommentModule") {
        bindViewModelWithArgs { args: CreateCommentMviModelParams ->
            CreateCommentViewModel(
                postId = args.postId,
                parentId = args.parentId,
                editedCommentId = args.editedCommentId,
                draftId = args.draftId,
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
