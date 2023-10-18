package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature.settings.main.SettingsMviModel
import org.koin.java.KoinJavaComponent.inject

actual fun getSettingsScreenModel(): SettingsMviModel {
    val res: SettingsMviModel by inject(SettingsMviModel::class.java)
    return res
}
