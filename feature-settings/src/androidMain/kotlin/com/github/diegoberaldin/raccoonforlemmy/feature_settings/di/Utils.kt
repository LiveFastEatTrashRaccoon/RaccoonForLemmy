package com.github.diegoberaldin.raccoonforlemmy.feature_settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenModel
import org.koin.java.KoinJavaComponent

actual fun getSettingsScreenModel(): SettingsScreenModel {
    val res: SettingsScreenModel by KoinJavaComponent.inject(SettingsScreenModel::class.java)
    return res
}