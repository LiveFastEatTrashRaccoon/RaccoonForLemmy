package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.main.ExploreViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions.ManageSubscriptionsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getExploreViewModel() = SearchScreenModelHelper.model

actual fun getManageSubscriptionsViewModel() = SearchScreenModelHelper.manageSuscriptionsViewModel

object SearchScreenModelHelper : KoinComponent {
    val model: ExploreViewModel by inject()
    val manageSuscriptionsViewModel: ManageSubscriptionsViewModel by inject()
}
