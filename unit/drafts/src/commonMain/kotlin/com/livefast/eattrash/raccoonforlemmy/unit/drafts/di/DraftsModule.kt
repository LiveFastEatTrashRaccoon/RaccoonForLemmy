package com.livefast.eattrash.raccoonforlemmy.unit.drafts.di

import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val draftsModule =
    DI.Module("DraftsModule") {
        bind<DraftsMviModel> {
            singleton {
                DraftsViewModel(
                    themeRepository = instance(),
                    accountRepository = instance(),
                    draftRepository = instance(),
                    notificationCenter = instance(),
            )
        }
    }
}
