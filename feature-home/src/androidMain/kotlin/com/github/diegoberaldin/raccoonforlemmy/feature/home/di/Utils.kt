package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListMviModel
import org.koin.java.KoinJavaComponent.inject

actual fun getHomeScreenModel(): PostListMviModel {
    val res: PostListMviModel by inject(PostListMviModel::class.java)
    return res
}