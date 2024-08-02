package com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di

import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListViewModel
import org.koin.dsl.module

val reportListModule =
    module {
        factory<ReportListMviModel> { params ->
            ReportListViewModel(
                communityId = params[0],
                identityRepository = get(),
                postRepository = get(),
                commentRepository = get(),
                themeRepository = get(),
                settingsRepository = get(),
                hapticFeedback = get(),
                notificationCenter = get(),
            )
        }
    }
