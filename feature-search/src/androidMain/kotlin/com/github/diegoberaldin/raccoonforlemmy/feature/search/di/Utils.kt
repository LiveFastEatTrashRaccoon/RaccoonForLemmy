package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.main.ExploreViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions.ManageSubscriptionsViewModel
import org.koin.java.KoinJavaComponent.inject

actual fun getExploreViewModel(): ExploreViewModel {
    val res: ExploreViewModel by inject(ExploreViewModel::class.java)
    return res
}

actual fun getManageSubscriptionsViewModel(): ManageSubscriptionsViewModel {
    val res: ManageSubscriptionsViewModel by inject(ManageSubscriptionsViewModel::class.java)
    return res
}

