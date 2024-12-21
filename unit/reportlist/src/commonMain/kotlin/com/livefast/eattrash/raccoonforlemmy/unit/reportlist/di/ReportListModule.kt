package com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di

import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val reportListModule =
    DI.Module("ReportListModule") {
        bind<ReportListMviModel> {
            factory { communityId: Long ->
                ReportListViewModel(
                    communityId = communityId,
                    identityRepository = instance(),
                    postRepository = instance(),
                    commentRepository = instance(),
                    themeRepository = instance(),
                    settingsRepository = instance(),
                    hapticFeedback = instance(),
                    notificationCenter = instance(),
            )
        }
    }
}
