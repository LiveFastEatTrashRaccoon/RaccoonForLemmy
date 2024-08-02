package com.livefast.eattrash.raccoonforlemmy.unit.createpost.di

import com.livefast.eattrash.raccoonforlemmy.unit.createpost.CreatePostMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.createpost.CreatePostViewModel
import org.koin.dsl.module

val createPostModule =
    module {
        factory<CreatePostMviModel> { params ->
            CreatePostViewModel(
                editedPostId = params[0],
                crossPostId = params[1],
                draftId = params[2],
                identityRepository = get(),
                postRepository = get(),
                mediaRepository = get(),
                siteRepository = get(),
                themeRepository = get(),
                settingsRepository = get(),
                itemCache = get(),
                communityRepository = get(),
                accountRepository = get(),
                draftRepository = get(),
                notificationCenter = get(),
                communityPreferredLanguageRepository = get(),
                lemmyValueCache = get(),
            )
        }
    }
