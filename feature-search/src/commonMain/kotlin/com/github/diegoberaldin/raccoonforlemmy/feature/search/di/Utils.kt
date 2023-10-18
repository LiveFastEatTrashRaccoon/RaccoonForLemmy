package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.main.ExploreMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions.ManageSubscriptionsMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.detail.MultiCommunityMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.multicommunity.editor.MultiCommunityEditorMviModel

expect fun getExploreViewModel(): ExploreMviModel

expect fun getManageSubscriptionsViewModel(): ManageSubscriptionsMviModel

expect fun getMultiCommunityViewModel(community: MultiCommunityModel): MultiCommunityMviModel

expect fun getMultiCommunityEditorViewModel(editedCommunity: MultiCommunityModel?): MultiCommunityEditorMviModel
