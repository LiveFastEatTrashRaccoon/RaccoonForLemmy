package com.github.diegoberaldin.raccoonforlemmy.settings

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSettingsScreenModel() = SettingsScreenModelHelper().model

class SettingsScreenModelHelper : KoinComponent {
    val model: SettingsScreenModel by inject()
}