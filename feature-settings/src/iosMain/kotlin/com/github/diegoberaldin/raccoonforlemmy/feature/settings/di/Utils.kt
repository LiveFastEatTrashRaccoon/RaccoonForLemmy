package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature.settings.main.SettingsMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSettingsScreenModel(): SettingsMviModel = SettingsScreenModelHelper.model

object SettingsScreenModelHelper : KoinComponent {
    val model: SettingsMviModel by inject()
}
