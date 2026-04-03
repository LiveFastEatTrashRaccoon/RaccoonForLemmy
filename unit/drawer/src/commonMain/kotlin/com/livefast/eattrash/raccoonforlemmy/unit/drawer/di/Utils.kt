package com.livefast.eattrash.raccoonforlemmy.unit.drawer.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.cache.SubscriptionsCache
import org.kodein.di.instance

fun getSubscriptionsCache(): SubscriptionsCache {
    val res by RootDI.di.instance<SubscriptionsCache>()
    return res
}

@Composable
fun rememberSubscriptionsCache(): SubscriptionsCache = remember { getSubscriptionsCache() }
