package com.github.diegoberaldin.raccoonforlemmy.feature_settings

import org.koin.core.module.Module

expect val settingsTabModule: Module

expect fun getSettingsScreenModel(): SettingsScreenModel
