package com.livefast.eattrash.raccoonforlemmy.core.api.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import org.kodein.di.instance

internal inline fun <reified T> getService(args: ServiceCreationArgs): T {
    val res: T by RootDI.di.instance(arg = args)
    return res
}
