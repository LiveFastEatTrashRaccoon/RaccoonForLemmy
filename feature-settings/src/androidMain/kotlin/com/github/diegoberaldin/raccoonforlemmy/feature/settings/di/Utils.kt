package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature.settings.content.SettingsScreenViewModel
import org.koin.java.KoinJavaComponent

actual fun getSettingsScreenModel(): SettingsScreenViewModel {
    val res: SettingsScreenViewModel by KoinJavaComponent.inject(SettingsScreenViewModel::class.java)
    return res
}
