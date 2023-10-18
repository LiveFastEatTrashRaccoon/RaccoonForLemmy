package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.main.ExploreMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions.ManageSubscriptionsMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.detail.MultiCommunityMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.editor.MultiCommunityEditorMviModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

actual fun getExploreViewModel(): ExploreMviModel {
    val res: ExploreMviModel by inject(ExploreMviModel::class.java)
    return res
}

actual fun getManageSubscriptionsViewModel(): ManageSubscriptionsMviModel {
    val res: ManageSubscriptionsMviModel by inject(ManageSubscriptionsMviModel::class.java)
    return res
}

actual fun getMultiCommunityViewModel(community: MultiCommunityModel): MultiCommunityMviModel {
    val res: MultiCommunityMviModel by inject(
        MultiCommunityMviModel::class.java,
        parameters = { parametersOf(community) }
    )
    return res
}

actual fun getMultiCommunityEditorViewModel(editedCommunity: MultiCommunityModel?): MultiCommunityEditorMviModel {
    val res: MultiCommunityEditorMviModel by inject(
        MultiCommunityEditorMviModel::class.java,
        parameters = { parametersOf(editedCommunity) }
    )
    return res
}
