package com.livefast.eattrash.raccoonforlemmy.unit.createpost.di

import com.livefast.eattrash.raccoonforlemmy.unit.createpost.CreatePostMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.CreatePostViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

internal data class CreatePostMviModelParams(val editedPostId: Long, val crossPostId: Long, val draftId: Long)

val createPostModule =
    DI.Module("CreatePostModule") {
        bind<CreatePostMviModel> {
            factory { params: CreatePostMviModelParams ->
                CreatePostViewModel(
                    editedPostId = params.editedPostId,
                    crossPostId = params.crossPostId,
                    draftId = params.draftId,
                    identityRepository = instance(),
                    postRepository = instance(),
                    mediaRepository = instance(),
                    siteRepository = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    itemCache = instance(),
                    communityRepository = instance(),
                    accountRepository = instance(),
                    draftRepository = instance(),
                    notificationCenter = instance(),
                    communityPreferredLanguageRepository = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
    }
