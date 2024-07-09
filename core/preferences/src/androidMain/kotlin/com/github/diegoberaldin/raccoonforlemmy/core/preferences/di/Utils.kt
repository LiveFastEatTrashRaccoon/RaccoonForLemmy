package com.github.diegoberaldin.raccoonforlemmy.core.preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.koin.java.KoinJavaComponent.inject

actual fun getAppConfigStore(): AppConfigStore {
    val res by inject<AppConfigStore>(AppConfigStore::class.java)
    return res
}
