package com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig

internal interface AppConfigDataSource {
    suspend fun get(): AppConfig

    suspend fun update(value: AppConfig)
}
