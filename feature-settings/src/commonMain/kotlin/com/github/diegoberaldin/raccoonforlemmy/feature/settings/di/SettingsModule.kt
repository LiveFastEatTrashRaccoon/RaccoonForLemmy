package com.github.diegoberaldin.raccoonforlemmy.feature.settings.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.viewmodel.SettingsScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.viewmodel.SettingsScreenViewModel
import org.koin.dsl.module

val settingsTabModule = module {
    factory {
        SettingsScreenViewModel(
            mvi = DefaultMviModel(
                SettingsScreenMviModel.UiState(),
            ),
            keyStore = get(),
            themeRepository = get(),
            languageRepository = get(),
            identityRepository = get(),
        )
    }
}
