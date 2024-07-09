package com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig

import kotlinx.coroutines.flow.StateFlow

interface AppConfigStore {
    val appConfig: StateFlow<AppConfig>

    fun initialize()
}
