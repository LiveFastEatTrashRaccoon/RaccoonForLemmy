package com.livefast.eattrash.raccoonforlemmy.unit.drafts.di

import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsViewModel
import org.koin.dsl.module

val draftsModule =
    module {
        factory<DraftsMviModel> {
            DraftsViewModel(
                themeRepository = get(),
                accountRepository = get(),
                draftRepository = get(),
                notificationCenter = get(),
            )
        }
    }
