package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CustomUriHandler
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

actual fun getApiConfigurationRepository(): ApiConfigurationRepository {
    val res: ApiConfigurationRepository by inject(ApiConfigurationRepository::class.java)
    return res
}

actual fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler {
    val res: CustomUriHandler by inject(
        clazz = CustomUriHandler::class.java,
        parameters = { parametersOf(fallbackUriHandler) },
    )
    return res
}
