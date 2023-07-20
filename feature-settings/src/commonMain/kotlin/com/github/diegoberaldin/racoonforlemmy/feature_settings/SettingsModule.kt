package com.github.diegoberaldin.racoonforlemmy.feature_settings

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsTabModule = module {
    factoryOf(::SettingsScreenModel)
}