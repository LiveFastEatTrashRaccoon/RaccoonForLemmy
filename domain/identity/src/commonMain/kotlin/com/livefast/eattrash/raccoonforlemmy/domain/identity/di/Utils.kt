package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CustomUriHandler

fun getApiConfigurationRepository(): ApiConfigurationRepository {
    val res by RootDI.di.instance<ApiConfigurationRepository>()
    return res
}

fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler {
    val res by RootDI.di.instance<CustomUriHandler>(arg = fallbackUriHandler)
    return res
}
