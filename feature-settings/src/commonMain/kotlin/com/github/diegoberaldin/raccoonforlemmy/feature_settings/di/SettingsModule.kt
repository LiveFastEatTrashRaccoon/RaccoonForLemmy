package com.github.diegoberaldin.raccoonforlemmy.feature_settings.di

import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenModel
import org.koin.core.module.Module

expect val settingsTabModule: Module

expect fun getSettingsScreenModel(): SettingsScreenModel
