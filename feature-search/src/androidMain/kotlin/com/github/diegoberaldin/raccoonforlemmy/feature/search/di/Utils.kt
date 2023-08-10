package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist.CommunityListViewModel
import org.koin.java.KoinJavaComponent.inject

actual fun getSearchScreenModel(): CommunityListViewModel {
    val res: CommunityListViewModel by inject(CommunityListViewModel::class.java)
    return res
}
