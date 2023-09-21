package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.content.ExploreViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getExploreViewModel() = SearchScreenModelHelper.model

object SearchScreenModelHelper : KoinComponent {
    val model: ExploreViewModel by inject()
}
