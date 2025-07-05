package com.livefast.eattrash.raccoonforlemmy.unit.replies.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.replies.InboxRepliesViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val repliesModule =
    DI.Module("RepliesModule") {
        bindViewModel {
            InboxRepliesViewModel(
                identityRepository = instance(),
                userRepository = instance(),
                commentRepository = instance(),
                themeRepository = instance(),
                hapticFeedback = instance(),
                coordinator = instance(),
                notificationCenter = instance(),
                settingsRepository = instance(),
                lemmyValueCache = instance(),
            )
        }
    }
