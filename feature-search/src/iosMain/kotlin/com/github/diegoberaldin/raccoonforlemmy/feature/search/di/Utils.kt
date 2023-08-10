package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist.CommunityListViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSearchScreenModel() = SearchScreenModelHelper.model

object SearchScreenModelHelper : KoinComponent {
    val model: CommunityListViewModel by inject()
}
