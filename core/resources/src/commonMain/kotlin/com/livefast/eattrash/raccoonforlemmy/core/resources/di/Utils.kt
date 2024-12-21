package com.livefast.eattrash.raccoonforlemmy.core.resources.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.resources.CoreResources
import org.kodein.di.instance

fun getCoreResources(): CoreResources {
    val res by RootDI.di.instance<CoreResources>()
    return res
}
