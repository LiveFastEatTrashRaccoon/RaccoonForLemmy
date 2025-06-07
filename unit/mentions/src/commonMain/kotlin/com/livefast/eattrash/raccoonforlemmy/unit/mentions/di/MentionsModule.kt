package com.livefast.eattrash.raccoonforlemmy.unit.mentions.di

import com.livefast.eattrash.raccoonforlemmy.unit.mentions.InboxMentionsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.InboxMentionsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val mentionsModule =
    DI.Module("MentionsModule") {
        bind<InboxMentionsMviModel> {
            provider {
                InboxMentionsViewModel(
                    identityRepository = instance(),
                    userRepository = instance(),
                    commentRepository = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    hapticFeedback = instance(),
                    coordinator = instance(),
                    notificationCenter = instance(),
                    lemmyValueCache = instance(),
                )
            }
        }
    }
