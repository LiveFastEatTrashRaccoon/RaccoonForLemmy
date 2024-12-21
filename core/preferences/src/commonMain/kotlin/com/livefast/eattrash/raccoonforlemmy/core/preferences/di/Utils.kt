package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore

fun getAppConfigStore(): AppConfigStore {
    val res by RootDI.di.instance<AppConfigStore>()
    return res
}
