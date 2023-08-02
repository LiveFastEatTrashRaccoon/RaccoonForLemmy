package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature.settings.viewmodel.SettingsScreenViewModel
import org.koin.java.KoinJavaComponent

actual fun getSettingsScreenModel(): SettingsScreenViewModel {
    val res: SettingsScreenViewModel by KoinJavaComponent.inject(SettingsScreenViewModel::class.java)
    return res
}
