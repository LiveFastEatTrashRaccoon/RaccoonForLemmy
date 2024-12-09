package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import androidx.compose.ui.platform.UriHandler
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler.CustomUriHandler

expect fun getApiConfigurationRepository(): ApiConfigurationRepository

expect fun getCustomUriHandler(fallbackUriHandler: UriHandler): CustomUriHandler
