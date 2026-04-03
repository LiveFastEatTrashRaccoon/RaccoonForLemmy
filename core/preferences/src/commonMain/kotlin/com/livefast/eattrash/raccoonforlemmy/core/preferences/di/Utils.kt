package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import org.kodein.di.instance

fun getAppConfigStore(): AppConfigStore {
    val res by RootDI.di.instance<AppConfigStore>()
    return res
}

@Composable
fun rememberAppConfigStore(): AppConfigStore = remember { getAppConfigStore() }
