package com.livefast.eattrash.raccoonforlemmy.unit.mentions.di

import com.livefast.eattrash.raccoonforlemmy.unit.mentions.InboxMentionsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.InboxMentionsViewModel
import org.koin.dsl.module

val inboxMentionsModule =
    module {
        factory<InboxMentionsMviModel> {
            InboxMentionsViewModel(
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
