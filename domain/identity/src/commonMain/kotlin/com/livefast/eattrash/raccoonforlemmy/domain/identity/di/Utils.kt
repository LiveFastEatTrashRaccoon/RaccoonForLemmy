package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CustomUriHandler
import org.kodein.di.instance

fun getApiConfigurationRepository(): ApiConfigurationRepository {
    val res by RootDI.di.instance<ApiConfigurationRepository>()
    return res
}

fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler {
    val res by RootDI.di.instance<UriHandler, CustomUriHandler>(arg = fallbackUriHandler)
    return res
}
