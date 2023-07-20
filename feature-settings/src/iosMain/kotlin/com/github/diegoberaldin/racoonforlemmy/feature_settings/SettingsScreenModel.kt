package com.github.diegoberaldin.racoonforlemmy.feature_settings

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSettingsScreenModel() = SettingsScreenModelHelper.model

object SettingsScreenModelHelper : KoinComponent {
    val model: SettingsScreenModel by inject()
}