package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation.NavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListViewModel
import org.koin.java.KoinJavaComponent.inject

actual fun getHomeScreenModel(): PostListViewModel {
    val res: PostListViewModel by inject(PostListViewModel::class.java)
    return res
}