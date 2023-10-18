package com.github.diegoberaldin.raccoonforlemmy.di

import com.github.diegoberaldin.raccoonforlemmy.MainScreenMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getMainViewModel(): MainScreenMviModel {
    return MainDiHelper.mainViewModel
}

internal object MainDiHelper : KoinComponent {
    val mainViewModel: MainScreenMviModel by inject()
}