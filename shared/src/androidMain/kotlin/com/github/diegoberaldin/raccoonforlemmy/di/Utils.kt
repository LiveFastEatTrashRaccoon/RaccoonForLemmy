package com.github.diegoberaldin.raccoonforlemmy.di

import com.github.diegoberaldin.raccoonforlemmy.MainScreenMviModel
import org.koin.java.KoinJavaComponent.inject

actual fun getMainViewModel(): MainScreenMviModel {
    val res: MainScreenMviModel by inject(MainScreenMviModel::class.java)
    return res
}