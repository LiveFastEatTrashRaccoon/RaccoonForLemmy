package com.livefast.eattrash.raccoonforlemmy.unit.drafts.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val draftsModule =
    DI.Module("DraftsModule") {
        bindViewModel {
            DraftsViewModel(
                themeRepository = instance(),
                accountRepository = instance(),
                draftRepository = instance(),
                notificationCenter = instance(),
            )
        }
    }
