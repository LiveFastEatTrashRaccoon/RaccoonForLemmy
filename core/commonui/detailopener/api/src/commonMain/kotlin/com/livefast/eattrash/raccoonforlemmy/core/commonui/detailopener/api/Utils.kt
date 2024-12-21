package com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import org.kodein.di.instance

fun getDetailOpener(): DetailOpener {
    val res by RootDI.di.instance<DetailOpener>()
    return res
}
