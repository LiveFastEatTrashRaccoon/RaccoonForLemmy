package com.livefast.eattrash.raccoonforlemmy.unit.replies.di

import com.livefast.eattrash.raccoonforlemmy.unit.replies.InboxRepliesMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.replies.InboxRepliesViewModel
import org.koin.dsl.module

val inboxRepliesModule =
    module {
        factory<InboxRepliesMviModel> {
            InboxRepliesViewModel(
                userRepository = get(),
                identityRepository = get(),
                commentRepository = get(),
                themeRepository = get(),
                settingsRepository = get(),
                hapticFeedback = get(),
                coordinator = get(),
                notificationCenter = get(),
                lemmyValueCache = get(),
            )
        }
    }
