package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.content.ExploreViewModel
import org.koin.java.KoinJavaComponent.inject

actual fun getExploreViewModel(): ExploreViewModel {
    val res: ExploreViewModel by inject(ExploreViewModel::class.java)
    return res
}
