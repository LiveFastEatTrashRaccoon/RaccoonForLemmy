package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature.settings.main.SettingsViewModel
import org.koin.java.KoinJavaComponent

actual fun getSettingsScreenModel(): SettingsViewModel {
    val res: SettingsViewModel by KoinJavaComponent.inject(SettingsViewModel::class.java)
    return res
}
