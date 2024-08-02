package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository

expect fun getApiConfigurationRepository(): ApiConfigurationRepository
