package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import kotlinx.coroutines.flow.StateFlow

interface AppConfigStore {
    val appConfig: StateFlow<AppConfig>

    fun initialize()
}
