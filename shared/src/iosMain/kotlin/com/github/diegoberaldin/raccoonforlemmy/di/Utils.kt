package com.github.diegoberaldin.raccoonforlemmy.di

import com.github.diegoberaldin.raccoonforlemmy.MainViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getMainViewModel(): MainViewModel {
    return MainDiHelper.mainViewModel
}

internal object MainDiHelper : KoinComponent {
    val mainViewModel: MainViewModel by inject()
}