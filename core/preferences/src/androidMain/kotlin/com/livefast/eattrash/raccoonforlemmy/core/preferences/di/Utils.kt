package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.koin.java.KoinJavaComponent.inject

actual fun getAppConfigStore(): AppConfigStore {
    val res by inject<AppConfigStore>(AppConfigStore::class.java)
    return res
}
