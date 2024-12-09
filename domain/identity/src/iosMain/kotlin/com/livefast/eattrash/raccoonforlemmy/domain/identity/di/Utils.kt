package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CustomUriHandler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getApiConfigurationRepository(): ApiConfigurationRepository = DomainIdentityDiHelper.apiConfigurationRepository

actual fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler =
    DomainIdentityDiHelper.getCustomUriHandler(fallbackUriHandler)

internal object DomainIdentityDiHelper : KoinComponent {
    val apiConfigurationRepository: ApiConfigurationRepository by inject()

    fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler {
        val res by inject<CustomUriHandler>(
            parameters = { parametersOf(fallbackUriHandler) },
        )
        return res
    }
}
