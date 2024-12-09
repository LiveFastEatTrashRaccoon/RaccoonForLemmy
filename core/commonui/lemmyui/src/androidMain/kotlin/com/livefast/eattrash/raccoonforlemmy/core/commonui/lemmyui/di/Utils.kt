package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import org.koin.java.KoinJavaComponent

actual fun getFabNestedScrollConnection(): FabNestedScrollConnection {
    val res: FabNestedScrollConnection by KoinJavaComponent.inject(FabNestedScrollConnection::class.java)
    return res
}
