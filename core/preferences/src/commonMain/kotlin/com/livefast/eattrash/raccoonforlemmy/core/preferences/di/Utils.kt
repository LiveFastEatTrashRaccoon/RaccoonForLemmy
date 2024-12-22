package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.kodein.di.instance

fun getAppConfigStore(): AppConfigStore {
    val res by RootDI.di.instance<AppConfigStore>()
    return res
}
