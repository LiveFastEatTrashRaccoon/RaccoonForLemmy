package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.di.getCustomUriHandler

@Stable
interface CustomUriHandler : UriHandler {
    fun openUri(uri: String, allowOpenExternal: Boolean)
}

@Composable
fun ProvideCustomUriHandler(content: @Composable () -> Unit) {
    val fallbackHandler = LocalUriHandler.current
    val customUriHandler = remember { getCustomUriHandler(fallbackHandler) }
    CompositionLocalProvider(
        value = LocalUriHandler provides customUriHandler,
        content = content,
    )
}
