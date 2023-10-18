package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.main.ExploreMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions.ManageSubscriptionsMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.detail.MultiCommunityMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.editor.MultiCommunityEditorMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getExploreViewModel(): ExploreMviModel = ExploreInjectHelper.model

actual fun getManageSubscriptionsViewModel(): ManageSubscriptionsMviModel =
    ExploreInjectHelper.manageSuscriptionsViewModel

actual fun getMultiCommunityViewModel(community: MultiCommunityModel): MultiCommunityMviModel =
    ExploreInjectHelper.getMultiCommunityViewModel(community)

actual fun getMultiCommunityEditorViewModel(editedCommunity: MultiCommunityModel?): MultiCommunityEditorMviModel =
    ExploreInjectHelper.getMultiCommunityEditorViewModel(editedCommunity)

object ExploreInjectHelper : KoinComponent {
    val model: ExploreMviModel by inject()
    val manageSuscriptionsViewModel: ManageSubscriptionsMviModel by inject()

    internal fun getMultiCommunityViewModel(community: MultiCommunityModel): MultiCommunityMviModel {
        val res: MultiCommunityMviModel by inject(parameters = { parametersOf(community) })
        return res
    }

    internal fun getMultiCommunityEditorViewModel(editedCommunity: MultiCommunityModel?): MultiCommunityEditorMviModel {
        val res: MultiCommunityEditorMviModel by inject(parameters = { parametersOf(editedCommunity) })
        return res
    }
}
