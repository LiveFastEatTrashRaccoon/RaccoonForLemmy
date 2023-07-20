package com.github.diegoberaldin.racoonforlemmy.feature_settings

import org.koin.java.KoinJavaComponent.inject

actual fun getSettingsScreenModel(): SettingsScreenModel {
    val res: SettingsScreenModel by inject(SettingsScreenModel::class.java)
    return res
}