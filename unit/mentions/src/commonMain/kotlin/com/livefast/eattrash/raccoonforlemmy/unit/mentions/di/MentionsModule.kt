package com.livefast.eattrash.raccoonforlemmy.unit.mentions.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.InboxMentionsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val mentionsModule =
    DI.Module("MentionsModule") {
        bindViewModel {
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
