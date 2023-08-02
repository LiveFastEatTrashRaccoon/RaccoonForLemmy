package com.github.diegoberaldin.raccoonforlemmy.feature_settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenViewModel
import org.koin.java.KoinJavaComponent

actual fun getSettingsScreenModel(): SettingsScreenViewModel {
    val res: SettingsScreenViewModel by KoinJavaComponent.inject(SettingsScreenViewModel::class.java)
    return res
}