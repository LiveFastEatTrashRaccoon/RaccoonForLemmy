package com.livefast.eattrash.raccoonforlemmy.core.resources.di

import com.livefast.eattrash.raccoonforlemmy.core.resources.CoreResources
import org.koin.java.KoinJavaComponent

actual fun getCoreResources(): CoreResources {
    val res by KoinJavaComponent.inject<CoreResources>(CoreResources::class.java)
    return res
}
