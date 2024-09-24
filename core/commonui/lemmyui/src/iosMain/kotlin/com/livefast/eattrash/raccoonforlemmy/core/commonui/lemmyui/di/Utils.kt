package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CustomUriHandler
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.FabNestedScrollConnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getFabNestedScrollConnection(): FabNestedScrollConnection = LemmyUiDiHelper.fabNestedScrollConnection

actual fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler = LemmyUiDiHelper.getCustomUriHandler(fallbackUriHandler)

object LemmyUiDiHelper : KoinComponent {
    val fabNestedScrollConnection: FabNestedScrollConnection by inject()

    fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler {
        val res by inject<CustomUriHandler>(
            parameters = { parametersOf(fallbackUriHandler) },
        )
        return res
    }
}
