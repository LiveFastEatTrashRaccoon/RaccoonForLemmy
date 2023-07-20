package com.github.diegoberaldin.raccoonforlemmy.settings

import org.koin.java.KoinJavaComponent.inject

actual fun getSettingsScreenModel(): SettingsScreenModel {
    val res: SettingsScreenModel by inject(SettingsScreenModel::class.java)
    return res
}