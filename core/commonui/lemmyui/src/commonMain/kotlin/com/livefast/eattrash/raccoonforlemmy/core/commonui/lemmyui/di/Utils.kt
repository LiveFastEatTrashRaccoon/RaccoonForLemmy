package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import org.kodein.di.instance

fun getFabNestedScrollConnection(): FabNestedScrollConnection {
    val res by RootDI.di.instance<FabNestedScrollConnection>()
    return res
}
