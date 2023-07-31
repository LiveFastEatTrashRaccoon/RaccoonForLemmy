package com.github.diegoberaldin.raccoonforlemmy.feature_settings.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenMviModel
import org.koin.dsl.module

val settingsTabModule = module {
    factory {
        SettingsScreenModel(
            mvi = DefaultMviModel(
                SettingsScreenMviModel.UiState()
            ),
            keyStore = get(),
            themeRepository = get(),
            languageRepository = get()
        )
    }
}
