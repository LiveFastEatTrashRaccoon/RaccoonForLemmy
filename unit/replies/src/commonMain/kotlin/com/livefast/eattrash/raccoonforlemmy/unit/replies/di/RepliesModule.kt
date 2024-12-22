package com.livefast.eattrash.raccoonforlemmy.unit.replies.di

import com.livefast.eattrash.raccoonforlemmy.unit.replies.InboxRepliesMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.replies.InboxRepliesViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val repliesModule =
    DI.Module("RepliesModule") {
        bind<InboxRepliesMviModel> {
            provider {
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
}
