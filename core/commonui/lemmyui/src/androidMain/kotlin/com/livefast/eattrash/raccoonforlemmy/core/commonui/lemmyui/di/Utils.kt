package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

actual fun getFabNestedScrollConnection(): FabNestedScrollConnection {
    val res: FabNestedScrollConnection by KoinJavaComponent.inject(FabNestedScrollConnection::class.java)
    return res
}

actual fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler {
    val res: CustomUriHandler by KoinJavaComponent.inject(
        clazz = CustomUriHandler::class.java,
        parameters = { parametersOf(fallbackUriHandler) },
    )
    return res
}
