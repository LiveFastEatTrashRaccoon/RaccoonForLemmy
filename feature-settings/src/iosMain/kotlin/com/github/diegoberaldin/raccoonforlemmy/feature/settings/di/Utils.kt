package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature.settings.main.SettingsViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSettingsScreenModel() = SettingsScreenModelHelper.model

object SettingsScreenModelHelper : KoinComponent {
    val model: SettingsViewModel by inject()
}
