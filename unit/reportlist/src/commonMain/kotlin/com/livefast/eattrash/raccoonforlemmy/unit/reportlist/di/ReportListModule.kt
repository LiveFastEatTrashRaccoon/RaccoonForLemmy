package com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class ReportListMviModelParams(val communityId: Long) : ViewModelCreationArgs

val reportListModule =
    DI.Module("ReportListModule") {
        bindViewModelWithArgs { args: ReportListMviModelParams ->
            ReportListViewModel(
                communityId = args.communityId,
                identityRepository = instance(),
                postRepository = instance(),
                commentRepository = instance(),
                themeRepository = instance(),
                settingsRepository = instance(),
                hapticFeedback = instance(),
            )
        }
    }
