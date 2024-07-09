package com.github.diegoberaldin.raccoonforlemmy.core.preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getAppConfigStore(): AppConfigStore = AppConfigDiHelper.appConfigStore

internal object AppConfigDiHelper : KoinComponent {
    val appConfigStore: AppConfigStore by inject()
}
