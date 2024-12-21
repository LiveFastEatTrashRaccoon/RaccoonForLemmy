package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.network.DefaultNetworkManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.network.NetworkManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal actual val nativeNetworkModule =
    DI.Module("NativeNetworkModule") {
        bind<NetworkManager> {
            singleton {
                DefaultNetworkManager(context = instance())
            }
        }
    }
