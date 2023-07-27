package com.github.diegoberaldin.raccoonforlemmy.feature_settings.di

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenMviModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject

actual val settingsTabModule = module {
    factory {
        SettingsScreenModel(
            themeRepository = get(ThemeRepository::class.java),
            keyStore = get(TemporaryKeyStore::class.java),
            mvi = DefaultMviModel(
                SettingsScreenMviModel.UiState()
            )
        )
    }
}

actual fun getSettingsScreenModel(): SettingsScreenModel {
    val res: SettingsScreenModel by inject(SettingsScreenModel::class.java)
    return res
}